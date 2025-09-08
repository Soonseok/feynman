import { Button, Heading, Text, VStack } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";

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
      <Heading>This Page gonna be a Main Page</Heading>
      <Text>But for now, it is just a test page</Text>
      <Text>{data}</Text>
      <Link to="/test-map">
        <Button colorScheme="red" variant={"ghost"}>
          Test Map Page &rarr;
        </Button>
      </Link>
      <Link to="/home">
        <Button colorScheme="red" variant={"ghost"}>
          Go Home(Not Working) &rarr;
        </Button>
      </Link>
    </VStack>
  );
}
