import axios from "axios";
import type {
  Product,
  Category,
  VideoReview,
  Cart,
  CartItem,
  Order,
  User,
  TokenResponse,
  PageResponse,
  Review,
  ReviewSummary,
  CreateReviewRequest,
} from "@/types";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/v1",
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  if (typeof window !== "undefined") {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

api.interceptors.response.use(
  (res) => res,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        try {
          const { data } = await axios.post<TokenResponse>(
            `${api.defaults.baseURL}/auth/refresh`,
            { refreshToken }
          );
          localStorage.setItem("accessToken", data.accessToken);
          localStorage.setItem("refreshToken", data.refreshToken);
          original.headers.Authorization = `Bearer ${data.accessToken}`;
          return api(original);
        } catch {
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
        }
      }
    }
    return Promise.reject(error);
  }
);

// Products
export const getProducts = (page = 0, size = 12, sort?: string) => {
  const params = new URLSearchParams({ page: String(page), size: String(size) });
  if (sort) params.append("sort", sort);
  return api.get<PageResponse<Product>>(`/products?${params}`).then((r) => r.data);
};

export const getProduct = (id: string) =>
  api.get<Product>(`/products/${id}`).then((r) => r.data);

export const searchProducts = (query: string, page = 0, size = 12, sort?: string) => {
  const params = new URLSearchParams({ q: query, page: String(page), size: String(size) });
  if (sort) params.append("sort", sort);
  return api.get<PageResponse<Product>>(`/products/search?${params}`).then((r) => r.data);
};

export const getProductsByCategory = (categoryId: string, page = 0, size = 12, sort?: string) => {
  const params = new URLSearchParams({ page: String(page), size: String(size) });
  if (sort) params.append("sort", sort);
  return api.get<PageResponse<Product>>(`/products/category/${categoryId}?${params}`).then((r) => r.data);
};

// Categories
export const getCategories = () =>
  api.get<Category[]>("/categories").then((r) => r.data);

export const getCategory = (id: string) =>
  api.get<Category>(`/categories/${id}`).then((r) => r.data);

// Video Reviews
export const getVideoReviews = (productId: string) =>
  api.get<VideoReview[]>(`/products/${productId}/videos`).then((r) => r.data);

// Reviews
export const getReviews = (productId: string) =>
  api.get<Review[]>(`/products/${productId}/reviews`).then((r) => r.data);

export const getReviewSummary = (productId: string) =>
  api.get<ReviewSummary>(`/products/${productId}/reviews/summary`).then((r) => r.data);

export const createReview = (productId: string, data: CreateReviewRequest) =>
  api.post<Review>(`/products/${productId}/reviews`, data).then((r) => r.data);

// Auth
export const requestCode = (identifier: string, method: "phone" | "email") =>
  api.post("/auth/request-code", method === "phone" ? { phone: identifier } : { email: identifier });

export const verifyCode = (identifier: string, code: string, method: "phone" | "email") =>
  api.post<TokenResponse>("/auth/verify", method === "phone"
    ? { phone: identifier, code }
    : { email: identifier, code }
  ).then((r) => r.data);

export const refreshToken = (token: string) =>
  api.post<TokenResponse>("/auth/refresh", { refreshToken: token }).then((r) => r.data);

// User
export const getMe = () =>
  api.get<User>("/users/me").then((r) => r.data);

export const updateMe = (name: string) =>
  api.put<User>("/users/me", { name }).then((r) => r.data);

export const linkTelegram = (chatId: number) =>
  api.post("/users/me/telegram", { chatId });

export const generateTelegramLink = () =>
  api.post<{ code: string; botLink: string }>("/telegram/link/generate").then((r) => r.data);

// Cart
export const getCart = () =>
  api.get<Cart>("/cart").then((r) => r.data);

export const addToCart = (item: Omit<CartItem, "quantity"> & { quantity?: number }) =>
  api.post<Cart>("/cart/items", { ...item, quantity: item.quantity || 1 }).then((r) => r.data);

export const removeFromCart = (productId: string) =>
  api.delete<Cart>(`/cart/items/${productId}`).then((r) => r.data);

export const clearCart = () =>
  api.delete("/cart");

// Orders
export const createOrder = (contact: { phone?: string; email?: string }) =>
  api.post<Order>("/orders", contact).then((r) => r.data);

export const getOrders = (page = 0, size = 10) =>
  api.get<PageResponse<Order>>(`/orders?page=${page}&size=${size}`).then((r) => r.data);

export const getOrder = (id: string) =>
  api.get<Order>(`/orders/${id}`).then((r) => r.data);

export default api;
