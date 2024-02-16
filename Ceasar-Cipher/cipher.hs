import Data.Char

{-

Name:  Adrian Clement
Date:  9/24/2023

CSCI 054 - Fall 2023

-}

-- sanitize
-- cleans the string; removes all char except spaces and letters, and makes all remaining letters uppercase
sanitize :: [Char] -> [Char]
sanitize [] = []
sanitize xs = filter (\a -> isLetter a || isSpace a) (map toUpper xs)


-- caesarOne
-- helper func to shift all char in a string by one
caesarOne :: [Char] -> [Char]
caesarOne [] = []
caesarOne (y:ys)
	| y == 'Z' = ' ' : caesarOne (ys)
	| y == ' ' = 'A' : caesarOne (ys)
	| otherwise = chr(ord y + 1) : caesarOne (ys)


-- caesar
-- shift all char in a cleaned/sanitized string by n amount
caesar :: Int -> [Char] -> [Char]
caesar n xs
	| n == 0 = as
	| n >= 27 = caesar (n-27) as
	| n < 0 = caesar (n+27) as
	| otherwise = caesar (n-1) (caesarOne as)
	where
		as = sanitize xs


-- keepFirst
-- goes through a list and removes duplicate elements, keeping the first iteration of each elem
keepFirst :: (Eq a) => [a] -> [a]
keepFirst xs
	| xs == [] = []
	| a `elem` as = keepFirst as
	| otherwise = keepFirst as ++ [a]
	where
		a = last xs
		as = init xs


-- subst
-- creates a list of tuple pairs that represent an encoding based on a given panagram
subst :: String -> [(Char, Char)]
subst [] = []
subst xs = zip "ABCDEFGHIJKLMNOPQRSTUVWXYZ " (keepFirst xs)


-- substEncripherOne
-- helper func to find desired encoding for each plaintext mesage char
substEncipherOne :: [(Char, Char)] -> Char -> Char
substEncipherOne (x:xs) y
	| a == y = b
	| otherwise = substEncipherOne xs y
	where
		(a, b) = x


-- substEncipher
-- encodes a plaintext message using a pangram string as a key
substEncipher :: String -> String -> String
substEncipher xs ys
	| length (sanitize ys) == 0 = ""
	| otherwise = (substEncipherOne zs w) : substEncipher xs ws
	where
		(w:ws) = sanitize ys
		zs = subst (sanitize xs)


-- substDecripherOne
-- helper func to decode each encoded char for plaintext message char
substDecipherOne :: [(Char, Char)] -> Char -> Char
substDecipherOne (x:xs) y
	| b == y = a
	| otherwise = substDecipherOne xs y
	where
		(a, b) = x


-- substDecipher
-- decodes an encoded message using a pangram string as a key
substDecipher :: String -> String -> String
substDecipher xs ys
	| length (sanitize ys) == 0 = ""
	| otherwise = (substDecipherOne zs w) : substDecipher xs ws
	where
		(w:ws) = sanitize ys
		zs = subst (sanitize xs)
