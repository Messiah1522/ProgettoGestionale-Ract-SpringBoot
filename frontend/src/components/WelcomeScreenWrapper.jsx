import { useEffect, useMemo, useState } from "react";
import { motion } from "framer-motion";
import { Sparkles, UserCircle2 } from "lucide-react";

const WelcomeScreenWrapper = ({ storageKey, nomeCompleto, children }) => {
  const [phase, setPhase] = useState(() => {
    if (storageKey && sessionStorage.getItem(storageKey) === "1") return "done";
    return "intro";
  });

  useEffect(() => {
    if (phase === "done") return undefined;

    const timerIntro = setTimeout(() => setPhase("move"), 1500);
    const timerDone = setTimeout(() => {
      setPhase("done");
      if (storageKey) {
        sessionStorage.setItem(storageKey, "1");
      }
    }, 2350);

    return () => {
      clearTimeout(timerIntro);
      clearTimeout(timerDone);
    };
  }, [phase, storageKey]);

  const showOverlay = phase !== "done";
  const showContent = phase === "done";

  const animatedState = useMemo(() => {
    if (phase === "move") {
      return {
        top: 18,
        right: 16,
        left: "auto",
        opacity: 0,
        scale: 0.5,
      };
    }
    return {
      top: "50%",
      left: "50%",
      opacity: 1,
      x: "-50%",
      y: "-50%",
      scale: 1,
    };
  }, [phase]);

  return (
    <div className="relative min-h-[calc(100vh-170px)]">
      {showOverlay && (
        <motion.div
          className="pointer-events-none fixed inset-0 z-[80] bg-white/80 backdrop-blur-md"
          initial={{ opacity: 1 }}
          animate={{ opacity: phase === "move" ? 0 : 1 }}
          transition={{ duration: 0.7 }}
        />
      )}

      {showOverlay && (
        <motion.div
          className="fixed z-[90] rounded-3xl border border-white/40 bg-gradient-to-br from-blue-600 to-sky-400 px-8 py-7 text-white shadow-2xl"
          initial={{
            top: "50%",
            left: "50%",
            x: "-50%",
            y: "-50%",
            opacity: 1,
            scale: 1,
          }}
          animate={animatedState}
          transition={{ duration: 0.75, ease: "easeInOut" }}
        >
          <div className="flex items-center gap-3">
            <div className="rounded-full bg-white/20 p-3">
              <UserCircle2 size={48} />
            </div>
            <div>
              <p className="text-xs font-semibold uppercase tracking-[0.18em] text-blue-100">
                Appia Padel
              </p>
              <h2 className="text-2xl font-black">Benvenuto {nomeCompleto}</h2>
              <p className="mt-1 flex items-center gap-1 text-sm text-sky-100">
                <Sparkles size={14} />
                Prepariamo la tua dashboard
              </p>
            </div>
          </div>
        </motion.div>
      )}

      <motion.div
        initial={{ opacity: 0, y: 24 }}
        animate={{
          opacity: showContent ? 1 : 0,
          y: showContent ? 0 : 24,
        }}
        transition={{ duration: 0.5, ease: "easeOut" }}
      >
        {showContent ? children : null}
      </motion.div>
    </div>
  );
};

export default WelcomeScreenWrapper;
