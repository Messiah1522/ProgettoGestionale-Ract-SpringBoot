import { createContext, useContext, useMemo, useState } from "react";
import { login as loginApi } from "../lib/api";
import {
  buildSessionFromLogin,
  clearSession,
  getStoredSession,
  saveSession,
} from "../lib/session";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [session, setSession] = useState(getStoredSession);

  const login = async (identifier, password) => {
    const payload = await loginApi({ identifier, password });
    const nextSession = buildSessionFromLogin(payload);
    saveSession(nextSession);
    setSession(nextSession);
    return nextSession;
  };

  const logout = () => {
    clearSession();
    setSession(null);
  };

  const value = useMemo(() => {
    const role = session?.viewerRole || "ROLE_USER";
    const isAdmin = role === "ROLE_ADMIN" || role === "ROLE_SUPERADMIN";
    return {
      session,
      role,
      isAdmin,
      isAuthenticated: Boolean(session?.token),
      login,
      logout,
    };
  }, [session]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth deve essere usato dentro AuthProvider");
  }
  return ctx;
};
