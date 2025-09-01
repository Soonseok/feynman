import { Box, Spacer } from "@chakra-ui/react"

export default function Footer() {
  return (
    <Box 
      as="footer" 
      py={4} 
      textAlign="center" 
      fontSize="sm" 
      color="gray.500" 
      borderColor="gray.200" 
      borderTop="1px solid"
    >
      <Spacer />
      (Ↄ) 2025 Feynman. Goott
      <br />
      ħ ∂/∂t |Ψ(t)&rsaquo; = Ĥ|Ψ(t)&rsaquo;
    </Box>
  )
}