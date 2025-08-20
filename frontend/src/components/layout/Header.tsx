import { Flex, Spacer, Box } from "@chakra-ui/react";
import LogoutButton from "../auth/LogoutButton";
import { ColorModeButton } from "../ui/color-mode";
import { Center } from "@chakra-ui/react";
import { HeaderLogoHome } from "./HeaderLogo";

export default function Header() {
  return (
    <Flex
      as="header"
      px={4}
      py={3}
      borderBottom="1px solid"
      borderColor="gray.200"
      align="center"
    >
      <HeaderLogoHome />
      <Spacer />
      <Box px={3}>
        <LogoutButton />
      </Box>
      <Center py={4}>
        <ColorModeButton />
      </Center>
    </Flex>
  );
}
