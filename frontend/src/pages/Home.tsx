import { Button, Heading, Image, VStack } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { ColorModeButton } from "../components/ui/color-mode";
import Footer from "../components/layout/Footer";
import Header from "../components/layout/Header";

export default function Home() {

    
    return (
        <VStack justifyContent={"center"} minH="100vh">
          <Header />
          <Heading>Just for Fun</Heading>
          <Image 
            src="/images/kkura.jpg" 
            rounded="md"
            w="75vw"
          />
          <Link to="/">
            <Button colorScheme="red" variant={"ghost"}>
              Go Main &rarr;
            </Button>
          </Link>
          <ColorModeButton />
          <Footer />
        </VStack>
      );
}