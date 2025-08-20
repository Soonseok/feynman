import { Box, IconButton, HStack } from "@chakra-ui/react";
import {
  HomeIcon,
  SearchIcon,
  CirclePlus,
  VideoIcon,
  UserIcon,
} from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useColorModeValue } from "../ui/color-mode";

export default function BottomNavBar() {
  const bgColor = useColorModeValue("rgb(255, 255, 255)", "rgb(0, 0, 0)");

  const location = useLocation();
  const navigate = useNavigate();

  const handleClickHome = () => {
    if (location.pathname === "/home") {
      window.scrollTo({ top: 0, behavior: "smooth" });
    } else {
      navigate("/home");
    }
  };

  return (
    <Box
      position="fixed"
      bottom="0"
      left="0"
      right="0"
      backgroundColor={bgColor}
      borderTop="1px solid"
      borderColor="gray.200"
      zIndex="banner"
      paddingBottom={10}
      marginTop={"1rem"}
    >
      <HStack justify="space-around">
        <Link to="/home">
          <IconButton aria-label="Home" variant="ghost" onClick={handleClickHome}/>
          <HomeIcon size={32} />
        </Link>

        <Link to="/search">
          <IconButton aria-label="Search" variant="ghost" />
          <SearchIcon size={32} />
        </Link>

        <Link to="/create">
          <IconButton aria-label="Create" variant="ghost" />
          <CirclePlus size={40} />
        </Link>

        <Link to="/reels">
          <IconButton aria-label="Reels" variant="ghost" />
          <VideoIcon size={32} />
        </Link>

        <Link to="/profile">
          <IconButton aria-label="Profile" variant="ghost" />
          <UserIcon size={32} />
        </Link>
      </HStack>
    </Box>
  );
}
