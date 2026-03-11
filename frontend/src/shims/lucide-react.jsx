import { forwardRef } from "react";

const IconBase = forwardRef(function IconBase(
  { children, size = 20, strokeWidth = 2, className = "", ...rest },
  ref
) {
  return (
    <svg
      ref={ref}
      xmlns="http://www.w3.org/2000/svg"
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={strokeWidth}
      strokeLinecap="round"
      strokeLinejoin="round"
      className={className}
      {...rest}
    >
      {children}
    </svg>
  );
});

export const CalendarDays = (props) => (
  <IconBase {...props}>
    <rect x="3" y="4" width="18" height="18" rx="2" />
    <line x1="16" y1="2" x2="16" y2="6" />
    <line x1="8" y1="2" x2="8" y2="6" />
    <line x1="3" y1="10" x2="21" y2="10" />
  </IconBase>
);

export const History = (props) => (
  <IconBase {...props}>
    <path d="M3 3v5h5" />
    <path d="M3.05 13a9 9 0 1 0 3-7.4L8 8" />
    <path d="M12 7v5l3 2" />
  </IconBase>
);

export const ShoppingCart = (props) => (
  <IconBase {...props}>
    <circle cx="9" cy="20" r="1" />
    <circle cx="18" cy="20" r="1" />
    <path d="M3 4h2l2.2 10.3a2 2 0 0 0 2 1.7H18a2 2 0 0 0 2-1.6L22 7H7" />
  </IconBase>
);

export const BarChart3 = (props) => (
  <IconBase {...props}>
    <line x1="12" y1="20" x2="12" y2="10" />
    <line x1="18" y1="20" x2="18" y2="4" />
    <line x1="6" y1="20" x2="6" y2="14" />
  </IconBase>
);

export const Store = (props) => (
  <IconBase {...props}>
    <path d="M3 10h18" />
    <path d="M5 10v9h14v-9" />
    <path d="M4 10 6 4h12l2 6" />
  </IconBase>
);

export const UserCircle2 = (props) => (
  <IconBase {...props}>
    <circle cx="12" cy="8" r="4" />
    <path d="M4 20a8 8 0 0 1 16 0" />
  </IconBase>
);

export const LogOut = (props) => (
  <IconBase {...props}>
    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
    <polyline points="16 17 21 12 16 7" />
    <line x1="21" y1="12" x2="9" y2="12" />
  </IconBase>
);

export const LogIn = (props) => (
  <IconBase {...props}>
    <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4" />
    <polyline points="10 17 15 12 10 7" />
    <line x1="15" y1="12" x2="3" y2="12" />
  </IconBase>
);

export const ShieldCheck = (props) => (
  <IconBase {...props}>
    <path d="M12 3 4 7v6c0 5 3.4 8.6 8 10 4.6-1.4 8-5 8-10V7z" />
    <path d="m9 12 2 2 4-4" />
  </IconBase>
);

export const Trophy = (props) => (
  <IconBase {...props}>
    <path d="M8 4h8v4a4 4 0 0 1-8 0z" />
    <path d="M6 8H4a2 2 0 0 1-2-2V5h4" />
    <path d="M18 8h2a2 2 0 0 0 2-2V5h-4" />
    <path d="M12 12v5" />
    <path d="M9 21h6" />
  </IconBase>
);

export const CreditCard = (props) => (
  <IconBase {...props}>
    <rect x="2" y="5" width="20" height="14" rx="2" />
    <line x1="2" y1="10" x2="22" y2="10" />
  </IconBase>
);

export const ClipboardList = (props) => (
  <IconBase {...props}>
    <rect x="8" y="2" width="8" height="4" rx="1" />
    <path d="M9 4H6a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2h-3" />
    <line x1="9" y1="12" x2="15" y2="12" />
    <line x1="9" y1="16" x2="15" y2="16" />
  </IconBase>
);

export const ChevronRight = (props) => (
  <IconBase {...props}>
    <polyline points="9 18 15 12 9 6" />
  </IconBase>
);

export const Sparkles = (props) => (
  <IconBase {...props}>
    <path d="m12 3 1.8 3.7L18 8.5l-3.2 3.1.8 4.4L12 14l-3.6 2 .8-4.4L6 8.5l4.2-1.8Z" />
  </IconBase>
);

export const Package = (props) => (
  <IconBase {...props}>
    <path d="m21 8-9-5-9 5 9 5 9-5Z" />
    <path d="M3 8v8l9 5 9-5V8" />
    <path d="m12 13 9-5" />
  </IconBase>
);
