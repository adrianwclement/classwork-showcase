import Data.Char(ord, chr)

{-

Name(s):  Adrian Clement
Date:  12/4/2023

CSCI 054 - Fall 2023

-}

-- squareMod
-- func that takes two int b n and returns b^2 mod n
squareMod :: Integer -> Integer -> Integer
squareMod 0 _ = 0
squareMod _ 0 = error "cannot divide by zero"
squareMod b n = ((b^2) `mod` n)


-- powerMod
-- func that takes three int b e n and returns b^e mod n via a recursive algorithm
powerMod :: Integer -> Integer -> Integer -> Integer
powerMod b e n
	| e == 0 = 1
	| x == 0 = squareMod (powerMod b (e `div` 2) n) n
	| otherwise = (b * squareMod (powerMod b (e `div` 2) n) n) `mod` n
	where
		x = e `mod` 2


-- block
-- func that converts an int m into its base-n representation
block :: Integer -> Integer -> [Integer]
block _ 0 = []
block n m = x : block n y
	where
		x = m `mod` n
		y = m `div` n


-- unblock
-- func that converts a list of int from its base-n representation into a conventional int
unblock :: Integer -> [Integer] -> Integer
unblock _ [] = 0
unblock n xs = y * (n ^ length ys) + (unblock n ys)
	where
		y = last xs
		ys = init xs


-- messageToInteger
-- func that encrypts a string into a list of int representing the string in base 256, and then converts that into a int
messageToInteger :: [Char] -> Integer
messageToInteger xs = unblock 256 (map (\char -> fromIntegral (ord char)) xs)


-- integerToMessage
-- func that converts an int into a list of int representing the string in base 256, and then convert each of these back to characters
integerToMessage :: Integer -> [Char]
integerToMessage x = map (\num -> chr (fromIntegral num)) (block 256 x)


-- rsaEncode
-- func that performs RSA encryption on a given integer message m using the public key (e, n)
rsaEncode :: (Integer, Integer) -> Integer -> Integer
rsaEncode (e, n) m = powerMod m e n


-- encodeString
-- func that encrypts a string into an integer using a public key 
encodeString :: (Integer, Integer) -> [Char] -> Integer
encodeString (e, n) xs = rsaEncode (e, n) (messageToInteger xs)


-- decodeString
-- func that decrypts an integer into a string using a private key 
decodeString :: (Integer, Integer) -> Integer -> [Char]
decodeString (d, n) xs = integerToMessage (rsaEncode (d, n) xs)

