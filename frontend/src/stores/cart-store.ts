import { create } from "zustand";
import type { Cart, CartItem } from "@/types";
import * as api from "@/lib/api";
import { useAuthStore } from "./auth-store";

interface CartState {
  cart: Cart | null;
  isLoading: boolean;
  itemCount: number;

  fetchCart: () => Promise<void>;
  addItem: (item: Omit<CartItem, "quantity"> & { quantity?: number }) => Promise<void>;
  removeItem: (productId: string) => Promise<void>;
  clear: () => Promise<void>;
}

const getLocalCart = (): Cart => {
  if (typeof window === "undefined") return { userId: "", items: [], totalPrice: 0 };
  const stored = localStorage.getItem("cart");
  if (stored) return JSON.parse(stored);
  return { userId: "", items: [], totalPrice: 0 };
};

const saveLocalCart = (cart: Cart) => {
  localStorage.setItem("cart", JSON.stringify(cart));
};

const calcTotal = (items: CartItem[]) =>
  items.reduce((sum, i) => sum + i.price * i.quantity, 0);

export const useCartStore = create<CartState>((set, get) => ({
  cart: null,
  isLoading: false,
  itemCount: 0,

  fetchCart: async () => {
    const { isAuthenticated } = useAuthStore.getState();
    if (isAuthenticated) {
      try {
        const localCart = getLocalCart();
        if (localCart.items.length > 0) {
          for (const item of localCart.items) {
            await api.addToCart(item);
          }
          localStorage.removeItem("cart");
        }
        const cart = await api.getCart();
        set({ cart, itemCount: cart.items.length });
      } catch {
        set({ cart: { userId: "", items: [], totalPrice: 0 }, itemCount: 0 });
      }
    } else {
      const cart = getLocalCart();
      set({ cart, itemCount: cart.items.length });
    }
  },

  addItem: async (item) => {
    const { isAuthenticated } = useAuthStore.getState();
    if (isAuthenticated) {
      try {
        const cart = await api.addToCart(item);
        set({ cart, itemCount: cart.items.length });
      } catch (e) {
        console.error("Failed to add to cart", e);
      }
    } else {
      const cart = getLocalCart();
      const existing = cart.items.find((i) => i.productId === item.productId);
      if (existing) {
        existing.quantity += item.quantity || 1;
      } else {
        cart.items.push({ ...item, quantity: item.quantity || 1 });
      }
      cart.totalPrice = calcTotal(cart.items);
      saveLocalCart(cart);
      set({ cart, itemCount: cart.items.length });
    }
  },

  removeItem: async (productId) => {
    const { isAuthenticated } = useAuthStore.getState();
    if (isAuthenticated) {
      try {
        const cart = await api.removeFromCart(productId);
        set({ cart, itemCount: cart.items.length });
      } catch (e) {
        console.error("Failed to remove from cart", e);
      }
    } else {
      const cart = getLocalCart();
      cart.items = cart.items.filter((i) => i.productId !== productId);
      cart.totalPrice = calcTotal(cart.items);
      saveLocalCart(cart);
      set({ cart, itemCount: cart.items.length });
    }
  },

  clear: async () => {
    const { isAuthenticated } = useAuthStore.getState();
    if (isAuthenticated) {
      await api.clearCart();
    }
    localStorage.removeItem("cart");
    set({ cart: { userId: "", items: [], totalPrice: 0 }, itemCount: 0 });
  },
}));
