import { createBrowserRouter, Outlet } from "react-router-dom"
import NotFound from "./pages/NotFound"
import Main from "./pages/Main"
import Home from "./pages/Home"
import BoundaryMapPage from "./pages/BoundaryMapPage"
import Header from "./components/layout/Header"
import Footer from "./components/layout/Footer"
import GeoserverTest from "./pages/GeoserverTest"

const router = createBrowserRouter([
    {
    path: "/",
    element: (
      <>
        <Header />
        <Outlet />
        <Footer />
      </>
    ),
    errorElement: <NotFound />,
    children: [
      { index: true, element: <Main /> },
      { path: "home", element: <Home /> },
      { path: "test-map", element: <BoundaryMapPage /> },
      { path: "geoserver-test", element: <GeoserverTest /> },
    ],
  },
])

export default router