import { create } from "zustand";
import type { User } from "@/types";
import { getMe, verifyCode, requestCode as apiRequestCode } from "@/lib/api";

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  showAuthModal: boolean;
  pendingAction: (() => void) | null;

  setShowAuthModal: (show: boolean, pendingAction?: (() => void) | null) => void;
  requestCode: (identifier: string, method: "phone" | "email") => Promise<void>;
  verify: (identifier: string, code: string, method: "phone" | "email") => Promise<void>;
  fetchUser: () => Promise<void>;
  logout: () => void;
  initAuth: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isAuthenticated: false,
  isLoading: true,
  showAuthModal: false,
  pendingAction: null,

  setShowAuthModal: (show, pendingAction = null) =>
    set({ showAuthModal: show, pendingAction }),

  requestCode: async (identifier: string, method: "phone" | "email") => {
    await apiRequestCode(identifier, method);
  },

  verify: async (identifier: string, code: string, method: "phone" | "email") => {
    const tokens = await verifyCode(identifier, code, method);
    localStorage.setItem("accessToken", tokens.accessToken);
    localStorage.setItem("refreshToken", tokens.refreshToken);
    await get().fetchUser();
    set({ showAuthModal: false });
    const pending = get().pendingAction;
    if (pending) {
      pending();
      set({ pendingAction: null });
    }
  },

  fetchUser: async () => {
    try {
      const user = await getMe();
      set({ user, isAuthenticated: true, isLoading: false });
    } catch {
      set({ user: null, isAuthenticated: false, isLoading: false });
    }
  },

  logout: () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    set({ user: null, isAuthenticated: false, showAuthModal: false, pendingAction: null });
  },

  initAuth: async () => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      await get().fetchUser();
    } else {
      set({ isLoading: false });
    }
  },
}));
