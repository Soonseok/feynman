import { Button, Heading, Text, VStack } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { ColorModeButton } from "../components/ui/color-mode";

export default function NotFound() {
  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Heading>Page Not Found</Heading>
      <Text>It seems that you're lost</Text>
      <Link to="/home">
        <Button colorScheme="red" variant={"ghost"}>
          Go Home &rarr;
        </Button>
      </Link>
      <Link to="/">
        <Button colorScheme="red" variant={"ghost"}>
          Go Login Page &rarr;
        </Button>
      </Link>
      <ColorModeButton />
    </VStack>
  );
}