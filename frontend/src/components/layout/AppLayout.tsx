import { Box } from "@chakra-ui/react"
import { Outlet } from "react-router-dom"
import BottomNavBar from "./BottomNavBar"

export default function AppLayout() {
  return (
    <Box minH="100vh" pb="60px"> {/* footer 공간 확보 */}
      <Outlet />
      <BottomNavBar />
    </Box>
  )
}
