import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const errorRate = new Rate('errors');
const productListDuration = new Trend('product_list_duration');
const productSearchDuration = new Trend('product_search_duration');

export const options = {
    stages: [
        { duration: '30s', target: 10 },   // ramp-up
        { duration: '1m', target: 50 },     // sustained load
        { duration: '30s', target: 100 },   // peak load
        { duration: '1m', target: 100 },    // sustained peak
        { duration: '30s', target: 0 },     // ramp-down
    ],
    thresholds: {
        http_req_duration: ['p(95)<500', 'p(99)<1000'],
        errors: ['rate<0.05'],
        product_list_duration: ['p(95)<400'],
        product_search_duration: ['p(95)<500'],
    },
};

export default function () {
    group('Product Listing', () => {
        const res = http.get(`${BASE_URL}/api/v1/products?page=0&size=20`);
        productListDuration.add(res.timings.duration);
        const success = check(res, {
            'products list returns 200': (r) => r.status === 200,
            'products list has content': (r) => JSON.parse(r.body).content !== undefined,
        });
        errorRate.add(!success);
    });

    sleep(0.5);

    group('Product Search', () => {
        const queries = ['iPhone', 'MacBook', 'Galaxy', 'PlayStation', 'AirPods'];
        const query = queries[Math.floor(Math.random() * queries.length)];
        const res = http.get(`${BASE_URL}/api/v1/products/search?q=${query}`);
        productSearchDuration.add(res.timings.duration);
        const success = check(res, {
            'search returns 200': (r) => r.status === 200,
        });
        errorRate.add(!success);
    });

    sleep(0.5);

    group('Categories', () => {
        const res = http.get(`${BASE_URL}/api/v1/categories`);
        const success = check(res, {
            'categories returns 200': (r) => r.status === 200,
            'categories is array': (r) => Array.isArray(JSON.parse(r.body)),
        });
        errorRate.add(!success);
    });

    sleep(1);
}
