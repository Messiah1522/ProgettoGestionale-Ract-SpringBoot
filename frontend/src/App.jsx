import { Navigate, Route, Routes, useLocation } from "react-router";
import Layout from "./components/Layout";
import Calendario from "./pages/Calendario";
import Partite from "./pages/Partite";
import Magazzino from "./pages/Magazzino";
import Cassa from "./pages/Cassa";
import Profilo from "./pages/Profilo";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Statistiche from "./pages/Statistiche";
import { useAuth } from "./context/AuthContext";

const RequireAuth = ({ children }) => {
  const location = useLocation();
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }
  return children;
};

const PublicOnly = ({ children }) => {
  const { isAuthenticated } = useAuth();
  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }
  return children;
};

function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={
          <PublicOnly>
            <Login />
          </PublicOnly>
        }
      />

      <Route
        path="/"
        element={
          <RequireAuth>
            <Layout />
          </RequireAuth>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="calendario" element={<Calendario />} />
        <Route path="partite" element={<Partite />} />
        <Route path="storico" element={<Partite />} />
        <Route path="magazzino" element={<Magazzino />} />
        <Route path="cassa" element={<Cassa />} />
        <Route path="statistiche" element={<Statistiche />} />
        <Route path="profilo" element={<Profilo />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
