import { Button, Grid, Heading, Image, VStack, Box } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { useState } from "react";
import PostDetailModal from "../components/layout/PostDetailModal";

export default function Home() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedImage, setSelectedImage] = useState<string | null>(null);

  const images = [
    "/images/CHAEYOUNG_250912_1.jpg",
    "/images/CHAEYOUNG_250912_2.jpg",
    "/images/CHAEYOUNG_250912_3.jpg",
    "/images/CHAEYOUNG_250912_4.jpg",
  ];

  const handleImageClick = (imageSrc: string) => {
    setSelectedImage(imageSrc);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedImage(null);
  };

  return (
    <VStack justifyContent={"center"} minH="100vh" marginY={5}>
      <Heading fontSize={"3xl"}>Just for Fun</Heading>
      <Link to="/">
        <Button colorScheme="red" variant={"ghost"}>
          Go Main &rarr;
        </Button>
      </Link>
      <Grid templateColumns="repeat(2, 2fr)" gap="6" w={"70vw"}>
        {images.map((src, index) => (
          <Box
            key={index}
            cursor="pointer"
            onClick={() => handleImageClick(src)}
          >
            <Image src={src} rounded="md" w="60vw" />
          </Box>
        ))}
      </Grid>
      {isModalOpen && (
        <PostDetailModal
          isOpen={isModalOpen}
          onClose={closeModal}
          imageSrc={selectedImage}
        />
      )}
    </VStack>
  );
}