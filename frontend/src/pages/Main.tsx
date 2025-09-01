import { Button, Heading, Text, VStack } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { ColorModeButton } from "../components/ui/color-mode";
import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";

export default function Main() {
    const [data, setData] = useState('')

    useEffect(() => {
        const fetchTime = () => {
            axios.get('/api/serverTime')
                .then(res => setData(res.data))
                .catch(err => console.log(err));
        };
        const intervalId = setInterval(fetchTime, 1000);
        return () => {
            clearInterval(intervalId);
        };
    }, []);

  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Header />
      <Heading>This Page gonna be a Main Page</Heading>
      <Text>But for now, it is just a test page</Text>
      <Text>{data}</Text>
      <Link to="/home">
        <Button colorScheme="red" variant={"ghost"}>
          Go Home(Not Working) &rarr;
        </Button>
      </Link>
      <ColorModeButton />
      <Footer />
    </VStack>
  );
}
