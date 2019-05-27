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
  `roomID` int,
  `cardNum` int,
  `checkIn` date,
  `checkOut` date,
  `numAdults` int,
  `numKids` int,
  `active` boolean,
  FOREIGN KEY (`custID`) REFERENCES `customer` (`id`),
  FOREIGN KEY (`roomID`) REFERENCES `room` (`roomID`),
  FOREIGN KEY (`cardNum`) REFERENCES `card` (`number`)

);

CREATE TABLE `card`
(
  `number` int PRIMARY KEY
);

CREATE TABLE `Customers`
(
  `id` int PRIMARY KEY,
  `firstName` varchar(255),
  `lastName` varchar(255)
);
