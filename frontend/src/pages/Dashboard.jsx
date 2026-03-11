import { useMemo } from "react";
import { useAuth } from "../context/AuthContext";
import WelcomeScreenWrapper from "../components/WelcomeScreenWrapper";
import UserDashboard from "../components/UserDashboard";
import AdminDashboard from "../components/AdminDashboard";

const Dashboard = () => {
  const { session, isAdmin } = useAuth();

  const displayName = useMemo(() => {
    if (!session) return "Giocatore";
    return `${session.nome || ""} ${session.cognome || ""}`.trim() || "Giocatore";
  }, [session]);

  const storageKey = session ? `appia-welcome-seen-${session.viewerUserId}` : "appia-welcome";

  return (
    <WelcomeScreenWrapper storageKey={storageKey} nomeCompleto={displayName}>
      {isAdmin ? (
        <AdminDashboard viewerRole={session.viewerRole} viewerUserId={session.viewerUserId} />
      ) : (
        <UserDashboard />
      )}
    </WelcomeScreenWrapper>
  );
};

export default Dashboard;
