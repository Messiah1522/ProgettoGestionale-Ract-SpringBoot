import { createElement, forwardRef, useEffect, useMemo, useState } from "react";

const toUnit = (value) => {
  if (value === undefined || value === null) return undefined;
  if (typeof value === "number") return `${value}px`;
  return String(value);
};

const mapMotionStateToStyle = (state = {}) => {
  const style = { ...state };
  const transforms = [];

  if (style.x !== undefined) {
    transforms.push(`translateX(${toUnit(style.x)})`);
    delete style.x;
  }
  if (style.y !== undefined) {
    transforms.push(`translateY(${toUnit(style.y)})`);
    delete style.y;
  }
  if (style.scale !== undefined) {
    transforms.push(`scale(${style.scale})`);
    delete style.scale;
  }
  if (style.rotate !== undefined) {
    transforms.push(`rotate(${style.rotate}deg)`);
    delete style.rotate;
  }

  if (transforms.length > 0) {
    style.transform = transforms.join(" ");
  }

  return style;
};

const MotionFactory = forwardRef(function MotionFactory(
  {
    as,
    initial,
    animate,
    transition,
    style,
    children,
    whileTap,
    whileHover,
    layoutId,
    exit,
    ...rest
  },
  ref
) {
  const [animatedStyle, setAnimatedStyle] = useState(() => mapMotionStateToStyle(initial));

  useEffect(() => {
    if (!animate) return;
    const frameId = requestAnimationFrame(() => {
      setAnimatedStyle(mapMotionStateToStyle(animate));
    });
    return () => cancelAnimationFrame(frameId);
  }, [animate]);

  const transitionStyle = useMemo(() => {
    const duration = transition?.duration ?? 0.45;
    const ease = typeof transition?.ease === "string" ? transition.ease : "ease";
    return `all ${duration}s ${ease}`;
  }, [transition]);

  return createElement(
    as,
    {
      ref,
      style: {
        transition: transitionStyle,
        ...style,
        ...animatedStyle,
      },
      ...rest,
    },
    children
  );
});

export const motion = new Proxy(
  {},
  {
    get(_target, tag) {
      const Component = forwardRef((props, ref) => <MotionFactory as={tag} ref={ref} {...props} />);
      Component.displayName = `Motion(${String(tag)})`;
      return Component;
    },
  }
);

export const AnimatePresence = ({ children }) => children;
