import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const JWT_TOKEN = __ENV.JWT_TOKEN || '';

const errorRate = new Rate('errors');
const addToCartDuration = new Trend('add_to_cart_duration');
const getCartDuration = new Trend('get_cart_duration');
const createOrderDuration = new Trend('create_order_duration');

export const options = {
    stages: [
        { duration: '20s', target: 5 },
        { duration: '1m', target: 20 },
        { duration: '30s', target: 40 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<1000', 'p(99)<2000'],
        errors: ['rate<0.1'],
        add_to_cart_duration: ['p(95)<500'],
        create_order_duration: ['p(95)<1000'],
    },
};

function authHeaders() {
    return {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${JWT_TOKEN}`,
        },
    };
}

function randomUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0;
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

export default function () {
    if (!JWT_TOKEN) {
        console.warn('JWT_TOKEN env var is required. Run with: k6 run -e JWT_TOKEN=<token> order-flow-load.js');
        return;
    }

    group('Add to Cart', () => {
        const payload = JSON.stringify({
            productId: randomUUID(),
            productName: 'iPhone 17 Pro',
            price: 129990.00,
            quantity: 1,
            imageUrl: 'https://example.com/img.jpg',
        });
        const res = http.post(`${BASE_URL}/api/v1/cart/items`, payload, authHeaders());
        addToCartDuration.add(res.timings.duration);
        const success = check(res, {
            'add to cart returns 200': (r) => r.status === 200,
        });
        errorRate.add(!success);
    });

    sleep(0.5);

    group('Get Cart', () => {
        const res = http.get(`${BASE_URL}/api/v1/cart`, authHeaders());
        getCartDuration.add(res.timings.duration);
        check(res, {
            'get cart returns 200': (r) => r.status === 200,
        });
    });

    sleep(0.5);

    group('Get Orders', () => {
        const res = http.get(`${BASE_URL}/api/v1/orders`, authHeaders());
        check(res, {
            'get orders returns 200': (r) => r.status === 200,
        });
    });

    sleep(1);
}
