select roomName, Month, sum(rev) as Revenue from
	(select roomName, MONTH(checkOut) as Month, r2.rate * DATEDIFF(checkOut, CheckIn) as Rev from
	Reservations r1 join Rooms r2 on r1.roomID = r2.roomID) as Prices
    group by roomName, Month
    order by roomName, Month;