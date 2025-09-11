import { Button, Heading, Image, VStack } from "@chakra-ui/react";
import { Link } from "react-router-dom";

export default function Home() {

    
    return (
        <VStack justifyContent={"center"} minH="100vh">
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
        </VStack>
      );
}