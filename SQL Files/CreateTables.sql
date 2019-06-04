CREATE TABLE `Rooms`
(
  `roomID` varchar(50) PRIMARY KEY,
  `roomName` varchar(255) UNIQUE,
  `numBeds` int,
  `bedType` varchar(255),
  `maxOccupancy` int,
  `rate` double,
  `decor` varchar(255)
);

CREATE TABLE `Reservations`
(
  `id` int PRIMARY KEY,
  `custID` int,
  `roomID` varchar(50),
  `cardNum` int,
  `checkIn` date,
  `checkOut` date,
  `numAdults` int,
  `numKids` int,
  `active` boolean,
  FOREIGN KEY (`custID`) REFERENCES `Customers` (`id`),
  FOREIGN KEY (`roomID`) REFERENCES `Rooms` (`roomID`),
  FOREIGN KEY (`cardNum`) REFERENCES `Cards` (`cardNum`)

);

CREATE TABLE `Cards`
(
  `cardNum` int PRIMARY KEY
);

CREATE TABLE `Customers`
(
  `id` int PRIMARY KEY,
  `firstName` varchar(255),
  `lastName` varchar(255)
);
