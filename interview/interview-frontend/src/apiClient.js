// src/apiClient.js
import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/prod'
});

apiClient.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

apiClient.interceptors.response.use(response => {
    if (response.data && response.data.code !== 200) {
        alert(response.data.message || '请求失败');
        return Promise.reject(response.data);
    }
    return response;
}, error => {
    if (error.response) {
        const message = error.response.data?.message || '无权限访问';
        alert(message);
    } else {
        alert('网络错误，请稍后重试');
    }
    return Promise.reject(error);
});

export { apiClient };
