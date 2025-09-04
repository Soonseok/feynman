import { Flex, Spacer } from "@chakra-ui/react";
// import { ColorModeButton } from "../ui/color-mode";
// import { Center } from "@chakra-ui/react";
import { HeaderLogoHome } from "./HeaderLogo";

export default function Header() {
  return (
    <Flex
      as="header"
      px={4}
      py={3}
      borderBottom="1px solid"
      borderColor="border.inverted"
      align="center"
    >
      <HeaderLogoHome />
      <Spacer />
      {/* <Center py={4}>
        <ColorModeButton />
      </Center> */}
    </Flex>
  );
}
