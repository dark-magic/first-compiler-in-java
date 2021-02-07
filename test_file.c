{
	define a as_a number.
	define b as_a number.

	on a check (a is 0)
	on a at 06:00 for 90 check (a is 0)

	on a let b be 4. check (a is 0)
	on a at 06:00 for 90 {let b be 5.} check (a is 0)

	on a at 06:00 check (a is 0)
	on a for 90 check (a is 0)

	//on a at 06:00 for 90000 {let b be 5.} check (a is 0)
	/*on a at 06:00 for 90 {
		on b check (b is 10)
	} check (a is 0)*/
}
