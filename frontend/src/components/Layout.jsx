import { useMemo } from "react";
import { NavLink, Navigate, Outlet, useLocation, useNavigate } from "react-router";
import {
  BarChart3,
  CalendarDays,
  CreditCard,
  History,
  LogOut,
  Package,
  Store,
  UserCircle2,
} from "lucide-react";
import { motion } from "framer-motion";
import { useAuth } from "../context/AuthContext";

const desktopNavItems = [
  { to: "/dashboard", label: "Dashboard", icon: UserCircle2, adminOnly: false, userOnly: false },
  { to: "/calendario", label: "Prenota", icon: CalendarDays, adminOnly: false, userOnly: false },
  { to: "/partite", label: "Partite", icon: History, adminOnly: false, userOnly: true },
  { to: "/cassa", label: "Cassa", icon: CreditCard, adminOnly: true, userOnly: false },
  { to: "/statistiche", label: "Statistiche", icon: BarChart3, adminOnly: true, userOnly: false },
  { to: "/magazzino", label: "Magazzino", icon: Store, adminOnly: true, userOnly: false },
  { to: "/profilo", label: "Profilo", icon: Package, adminOnly: false, userOnly: false },
];

const mobileUserNavItems = [
  { to: "/dashboard", label: "Home", icon: UserCircle2 },
  { to: "/calendario", label: "Prenota", icon: CalendarDays },
  { to: "/storico", label: "Storico", icon: History },
  { to: "/profilo", label: "Profilo", icon: Package },
];

const mobileAdminNavItems = [
  { to: "/dashboard", label: "Home", icon: UserCircle2 },
  { to: "/calendario", label: "Prenota", icon: CalendarDays },
  { to: "/cassa", label: "Cassa", icon: CreditCard },
  { to: "/statistiche", label: "Stats", icon: BarChart3 },
  { to: "/profilo", label: "Profilo", icon: Package },
];

const roleLabel = (role) => {
  if (role === "ROLE_SUPERADMIN") return "SuperAdmin";
  if (role === "ROLE_ADMIN") return "Admin";
  return "User";
};

const Layout = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { session, isAdmin, isAuthenticated, logout } = useAuth();

  if (!isAuthenticated || !session) {
    return <Navigate to="/login" replace />;
  }

  const filteredDesktopNav = desktopNavItems.filter((item) => {
    if (item.adminOnly && !isAdmin) return false;
    if (item.userOnly && isAdmin) return false;
    return true;
  });

  const mobileNav = isAdmin ? mobileAdminNavItems : mobileUserNavItems;

  const currentTitle = useMemo(() => {
    const all = [...desktopNavItems, ...mobileUserNavItems, ...mobileAdminNavItems];
    const item = all.find((nav) => location.pathname.startsWith(nav.to));
    return item ? item.label : "Dashboard";
  }, [location.pathname]);

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  const outletContext = useMemo(
    () => ({
      viewerRole: session.viewerRole,
      viewerUserId: session.viewerUserId,
      isAdmin,
      session,
    }),
    [session, isAdmin]
  );

  return (
    <div className="min-h-screen bg-white text-slate-900">
      <header className="sticky top-0 z-50 bg-gradient-to-r from-blue-600/80 to-sky-400/80 backdrop-blur-md">
        <div className="mx-auto flex w-full max-w-6xl items-center justify-between gap-3 px-4 py-3 text-white">
          <div>
            <h1 className="text-xl font-black tracking-tight">
              <span>APPIA</span> <span className="text-red-200">PADEL</span>
            </h1>
            <p className="text-xs font-semibold text-blue-100">{currentTitle}</p>
          </div>

          <div className="flex items-center gap-2">
            <div className="hidden rounded-2xl border border-white/35 bg-white/20 px-3 py-2 text-right md:block">
              <p className="text-xs font-black">
                {session.nome} {session.cognome}
              </p>
              <p className="text-[11px] text-blue-100">
                {roleLabel(session.viewerRole)} • #{session.viewerUserId}
              </p>
            </div>
            <button
              type="button"
              onClick={handleLogout}
              className="inline-flex items-center gap-1 rounded-xl bg-red-500 px-3 py-2 text-xs font-black text-white shadow-lg shadow-red-500/40 hover:bg-red-600"
            >
              <LogOut size={14} />
              Logout
            </button>
          </div>
        </div>

        <div className="mx-auto hidden w-full max-w-6xl flex-wrap gap-2 px-4 pb-3 md:flex">
          {filteredDesktopNav.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  `inline-flex items-center gap-2 rounded-xl px-3 py-2 text-sm font-semibold transition ${
                    isActive
                      ? "bg-red-500 text-white shadow-lg shadow-red-500/40"
                      : "bg-white/15 text-white hover:bg-white/25"
                  }`
                }
              >
                <Icon size={15} />
                {item.label}
              </NavLink>
            );
          })}
        </div>
      </header>

      <motion.main
        className="mx-auto w-full max-w-6xl px-4 pb-24 pt-4 md:pb-10"
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <Outlet context={outletContext} />
      </motion.main>

      <nav className="fixed bottom-0 left-0 right-0 z-40 border-t border-slate-200 bg-white/95 backdrop-blur md:hidden">
        <div
          className="mx-auto grid max-w-6xl"
          style={{ gridTemplateColumns: `repeat(${mobileNav.length}, minmax(0, 1fr))` }}
        >
          {mobileNav.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  `flex min-h-16 flex-col items-center justify-center gap-1 text-[11px] font-bold ${
                    isActive ? "text-red-500" : "text-slate-500"
                  }`
                }
              >
                <Icon size={18} />
                {item.label}
              </NavLink>
            );
          })}
        </div>
      </nav>
    </div>
  );
};

export default Layout;
