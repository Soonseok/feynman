import { createBrowserRouter } from "react-router-dom"
import NotFound from "./pages/NotFound"
import Main from "./pages/Main"
import Home from "./pages/Home"

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
])

export default router