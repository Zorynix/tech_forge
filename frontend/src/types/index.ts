export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  categoryId: string;
  categoryName: string;
  imageUrls: string[];
  spinImages: string[];
  modelUrl: string | null;
  specifications: Record<string, string>;
  stockQuantity: number;
  active: boolean;
  averageRating: number | null;
  reviewCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface Category {
  id: string;
  name: string;
  slug: string;
  description: string;
  parentCategoryId: string | null;
  imageUrl: string | null;
}

export interface VideoReview {
  id: string;
  productId: string;
  title: string;
  videoUrl: string;
  thumbnailUrl: string;
  durationSeconds: number;
  createdAt: string;
}

export interface CartItem {
  productId: string;
  productName: string;
  price: number;
  quantity: number;
  imageUrl: string;
}

export interface Cart {
  userId: string;
  items: CartItem[];
  totalPrice: number;
}

export type OrderStatus =
  | "CREATED"
  | "CONFIRMED"
  | "PROCESSING"
  | "READY_FOR_PICKUP"
  | "COMPLETED"
  | "CANCELLED";

export interface OrderItem {
  productId: string;
  productName: string;
  price: number;
  quantity: number;
  imageUrl: string;
}

export interface Order {
  id: string;
  userId: string;
  items: OrderItem[];
  status: OrderStatus;
  totalPrice: number;
  phone: string;
  createdAt: string;
  updatedAt: string;
}

export interface User {
  id: string;
  phone: string | null;
  email: string | null;
  name: string | null;
  telegramChatId: number | null;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface Notification {
  id: string;
  orderId: string;
  channel: string;
  message: string;
  status: string;
  sentAt: string | null;
  createdAt: string;
}

export interface Review {
  id: string;
  productId: string;
  authorName: string;
  title: string | null;
  content: string;
  ratingQuality: number;
  ratingPrice: number;
  ratingDesign: number;
  ratingFeatures: number;
  ratingUsability: number;
  overallRating: number;
  createdAt: string;
}

export interface ReviewSummary {
  productId: string;
  averageQuality: number;
  averagePrice: number;
  averageDesign: number;
  averageFeatures: number;
  averageUsability: number;
  averageOverall: number;
  totalReviews: number;
}

export interface CreateReviewRequest {
  authorName: string;
  title?: string;
  content: string;
  ratingQuality: number;
  ratingPrice: number;
  ratingDesign: number;
  ratingFeatures: number;
  ratingUsability: number;
}
