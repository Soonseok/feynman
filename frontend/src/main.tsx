import React from "react";
import ReactDOM from "react-dom/client";
import { Provider } from "./components/ui/provider";
import { Toaster } from "./components/ui/toaster";
import { RouterProvider } from "react-router-dom";
import router from "./Router";
import { ColorModeButton } from "./components/ui/color-mode";
import { Center } from "@chakra-ui/react";
import Footer from "./components/layout/Footer";

const showColorModeButton = ["/"].includes(location.pathname);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <Provider>
      <Toaster />
      <RouterProvider router={router} />
      <Center py={4}>{showColorModeButton && <ColorModeButton />}</Center>
      {location.pathname === "/" && <Footer />}
    </Provider>
  </React.StrictMode>
);
