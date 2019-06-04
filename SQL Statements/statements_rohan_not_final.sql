--- The system shall display the list of reservations made by a user including active and past reservations.
select firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where firstName = ? and lastname = ?
---?1: first name
---?2: last name


--- Upon the cancellation or change of a reservation, the system shall display the details of the cancelled or changed reservation on the screen.
select Reservations.id, firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where Reservations.id = ?
---?1: reservation id passed in from change function


--- Upon reservation, the system shall display the details of the reservation on the Screen.
select Reservations.id, firstName, lastName, roomName, checkIn, checkOut, rate from Reservations join Customers on Reservations.custID = Customers.id join Rooms on Reservations.roomID = Rooms.roomID where Reservations.id = ?
---?1: reservation id passed in from create function


--- The system shall allow a user to change reservations such as changing datesand changing rooms.
update Reservations set roomID = ?, checkIn = ?, checkout = ? where id = ?
---?1: roomID (if needed)
---?2: checkin (if needed)
---?3: check out (if needed)
---?4: reservation id


--- The system shall allow users to search for availabilities of rooms specifying day
---(checkout and checkin dates), the type of room (single, double, twin, etc), the
---decor, the price range, the number of rooms, and the number of occupants.
select roomName, numBeds, bedType, maxOccupancy, rate, decor from Rooms where bedType = ? and decor = ? and rate < ? and rate > ? and maxOccupancy >= ? and roomID not in (select roomID from Reservations where checkIn >= ? and checkOut <= ? and roomID in (select roomID from Rooms where bedType = ? and decor = ? and rate < ? and rate > ? and maxOccupancy >= ?))
---?1: bedType (Double, Queen, King)
---?2: decor (traditional, modern, rustic)
---?3: max rate
---?4: min rate
---?5: number of guests
---?6 check in date
---?7: check out date
---?8: bedType (Double, Queen, King)
---?9: decor (traditional, modern, rustic)
---?10: max rate
---?11: min rate
---?12: number of guests