{
	define a as_a number.
	define b as_a number.
	define c as_a number.
	define d as_a number.
	define result as_a number.
	define name as_a string.

	let name be "me".
	let a be ((5 plus 7) minus 10) to_the_power_of 3.
	let b be a times 27.
	let c be 23 modulo 2 times -1.
	let d be 0.

	if(5 is_less_than b is_less_than 10) {
		let b be 0 minus b.
	}

	do {
		let c be c plus 1.
	}
	until(c is_more_than 0).

	if(a is 5 times 3 and not (a is_not 0 or b is 3)) {
		for e be 3 to 4 let result be 1.

		for f be b to b by b {
			let d be b plus b.
		}
	}
	else if not(a is b) {
		do let a be a plus b. while (a is_not b).
		let result be 2.
	}
	else if(b minus a is c) {
		while (b minus a is c and not (c is 0))
		{
			let result be 3.
			let b be b plus 1.
		}
	}
	else
		until(result is 4) let result be 4.

	define me as_a bool.
	define he as_a bool.
	let he be false.

	unless(he is false) let me be he.

	look_at (result) {
		if_is 1 {
			let result be -1.
		}

		if_is 2 {
			let result be -2.
		}

		if_is 3 {
			let result be -3.
		}

		if_is 4 {
			let result be -4.
		}

		else {
			let result be +69.
		}
	}

	define she as_a bool.
	define free as_a bool.
	let free be true.

	if(she is free is true) {
		let she be false.
	}
}
