import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const errorRate = new Rate('errors');
const requestCodeDuration = new Trend('request_code_duration');

export const options = {
    stages: [
        { duration: '20s', target: 10 },
        { duration: '1m', target: 30 },
        { duration: '30s', target: 50 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<800', 'p(99)<1500'],
        errors: ['rate<0.1'],
        request_code_duration: ['p(95)<600'],
    },
};

export default function () {
    const phone = `+7900${String(Math.floor(Math.random() * 10000000)).padStart(7, '0')}`;

    group('Request Verification Code', () => {
        const payload = JSON.stringify({ phone: phone, email: null });
        const params = { headers: { 'Content-Type': 'application/json' } };
        const res = http.post(`${BASE_URL}/api/v1/auth/request-code`, payload, params);
        requestCodeDuration.add(res.timings.duration);
        const success = check(res, {
            'request code returns 200': (r) => r.status === 200,
        });
        errorRate.add(!success);
    });

    sleep(1);
}
