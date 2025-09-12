import {
  Dialog,
  Box,
  Image,
  Portal,
  CloseButton,
} from "@chakra-ui/react";

interface PostDetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  imageSrc: string | null;
}

export default function PostDetailModal({ isOpen, onClose, imageSrc }: PostDetailModalProps) {
  return (
    <Dialog.Root open={isOpen} onOpenChange={onClose}>
      <Portal>
        <Dialog.Backdrop />
        <Dialog.Positioner>
          <Dialog.Content maxW="90vw" minW="auto" p={2} borderRadius="lg" alignItems={"center"}>
            <Dialog.CloseTrigger asChild>
              <CloseButton position="absolute" top={2} right={2} />
            </Dialog.CloseTrigger>
            <Box>
              <Image
                src={imageSrc ?? undefined}
                w="auto"
                h="auto"
                objectFit="contain"
                alt="preview"
              />
            </Box>
          </Dialog.Content>
        </Dialog.Positioner>
      </Portal>
    </Dialog.Root>
  );
}