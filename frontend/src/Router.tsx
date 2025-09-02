import { createBrowserRouter } from "react-router-dom"
import NotFound from "./pages/NotFound"
import Main from "./pages/Main"
import Home from "./pages/Home"
import TestMap from "./pages/TestMap"

const router = createBrowserRouter([
    {
        path: "/",
        element: <Main />,
        errorElement: <NotFound />,
    },
    {
        path: "/home",
        element: <Home />,
        errorElement: <NotFound />,
    },
    {
        path: "/test-map",
        element: <TestMap />,
        errorElement: <NotFound />,
    },
])

export default router