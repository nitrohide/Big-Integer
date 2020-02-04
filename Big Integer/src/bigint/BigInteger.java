package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
			throws IllegalArgumentException {
		BigInteger bigInt = new BigInteger();
		integer = integer.trim();
		String s = integer;
		
		if (integer.length() == 0 || integer == null) { 
			throw new IllegalArgumentException("Incorrect format");
		}
		if (integer.equals("0")){
			bigInt.negative = false; 
			bigInt.front = null;
			bigInt.numDigits = 0;
			return null;
		}
		if (integer.charAt(0)== '-' || integer.charAt(0)== '+') {
			integer = integer.substring(1);
		}
		while(integer.length() > 1 && integer.indexOf("0") == 0) {
			integer = integer.substring(1);
		}
		if (integer.equals("0")){
			bigInt.negative = false; 
			bigInt.front = null;
			bigInt.numDigits = 0;
			return bigInt;
		}
		if (s.charAt(0) == '-' && integer != "0") { 
			bigInt.negative = true;
		}
		for (int i=0; i<integer.length(); i++) {
			char c = integer.charAt(i);
			if (Character.isDigit(c)==false){
				throw new IllegalArgumentException("Incorrect format");
			}
			else {
				int digitInt=integer.charAt(integer.length()-i-1)-'0';
				//int digitInt = integer.charAt(i) - '0';
				bigInt.front = new DigitNode(digitInt,bigInt.front); 
				bigInt.numDigits++;
				System.out.println(bigInt);
			}
		}
		System.out.println(bigInt.negative);
		System.out.println(bigInt.numDigits);
		bigInt.reverseList();
		return bigInt; 
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger bigInt = new BigInteger();
		int total = 0;
		if(first == null && second == null){
			return null;
		}
		if(first == null || first.front==null) {
			return second;	
		}
		if(second == null || second.front==null) {
			return first;
		}
		if(first.negative == second.negative) {
			bigInt.negative = first.negative;
			bigInt.front = new DigitNode(0, null);
			DigitNode ptr1 = first.front;
			DigitNode ptr2 = second.front;
			DigitNode ansPtr = bigInt.front;
			for (; ptr1 != null && ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next) {	
				int digitSum = ptr1.digit + ptr2.digit + total;
				total = digitSum / 10;
				ansPtr.digit = digitSum % 10;
				if(ptr1.next != null || ptr2.next != null) {
					ansPtr.next = new DigitNode(0,null);
					ansPtr = ansPtr.next;
				}
			}
			for(; ptr1 != null ; ptr1 = ptr1.next) {
				int digitSum = ptr1.digit + total;
				total = digitSum / 10;
				ansPtr.digit = digitSum % 10;
				if(ptr1.next != null) {
					ansPtr.next = new DigitNode(0,null);
					ansPtr = ansPtr.next;
				}
			}
			for(; ptr2 != null; ptr2 = ptr2.next) {
				int digitSum = ptr2.digit + total;
				total = digitSum / 10;
				ansPtr.digit = digitSum % 10;
				if(ptr2.next != null) {
					ansPtr.next = new DigitNode(0,null);
					ansPtr = ansPtr.next;
				}
			}
			if(total != 0) {
				ansPtr.next = new DigitNode(total, null);
			}
		}
		else {
			BigInteger greater;
			BigInteger lesser;
			if(greaterThan(first, second)) {
				greater = second;
				lesser = first;
			}
			else {
				greater = first;
				lesser = second;
			}
			bigInt.negative = greater.negative;
			bigInt.front = new DigitNode(0, null);
			DigitNode ptr1 = greater.front;
			DigitNode ptr2 = lesser.front;
			DigitNode ansPtr = bigInt.front;
			boolean	borrowed = false;
			boolean hasBorrowed = false; 
			for (; ptr1 != null && ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next, ansPtr = ansPtr.next) {			
				int bigDigit = ptr1.digit;
				if (borrowed) {
					bigDigit--;
				}
				if (bigDigit < ptr2.digit || bigDigit < 0) {
					bigDigit += 10;
					hasBorrowed = true;
				}
				int difference = bigDigit - ptr2.digit;
				ansPtr.digit = difference;
				if (ptr1.next != null || ptr2.next != null) {
					ansPtr.next = new DigitNode(0,null);
				}
				borrowed = hasBorrowed;
				hasBorrowed = false;
			}
			for(; ptr1 != null; ptr1 = ptr1.next) {
				int bigDigit = ptr1.digit;
				if(borrowed)
					bigDigit--;	
				if(bigDigit < 0) {
					bigDigit += 10;
					hasBorrowed = true;
				}
				ansPtr.digit = bigDigit;
				if(ptr1.next != null) {
					ansPtr.next = new DigitNode(0,null);
					ansPtr = ansPtr.next;
				}
				borrowed = hasBorrowed;
				hasBorrowed = false;
			}	
		}
		int count = 0;
		int lastNonzero = 0;
		bigInt.numDigits = 0;
		for(DigitNode ptr = bigInt.front; ptr != null; ptr = ptr.next, count++){
			if(ptr.digit != 0) {
				lastNonzero = count;
			}
		}
		count = 0;
		for(DigitNode ptr = bigInt.front; ptr != null; ptr = ptr.next, count++) {
			bigInt.numDigits++;
			if(count == lastNonzero) {
				ptr.next = null;
				break;
			}
		}
		if(bigInt.numDigits == 1 && bigInt.front.digit == 0 || bigInt.numDigits == 0) {
			bigInt.numDigits = 0;
			bigInt.negative = false;
			bigInt.front = null;	
		}
		return bigInt;
	}

		
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
		public static BigInteger multiply(BigInteger first, BigInteger second) {
			BigInteger bigInt = new BigInteger();	
			if(first == null || second == null || second.front == null || first.front == null) {
				bigInt.negative = false;
				bigInt.front = null;
				bigInt.numDigits = 0;
				return bigInt;
			}
			bigInt.front = new DigitNode(0, null);
			int leadingZeros = 0;
			for(DigitNode ptr2 = second.front; ptr2 != null; ptr2 = ptr2.next, leadingZeros++) {
				BigInteger nextNode = new BigInteger();
				nextNode.front = new DigitNode(0, null);
				DigitNode Node = nextNode.front;
				for(int i = 0; i < leadingZeros; i++) {
					Node.digit = 0;
					Node.next = new DigitNode(0,null);
					Node = Node.next;
				}
				int carryOver = 0;
				for(DigitNode ptr1 = first.front; ptr1 != null; ptr1 = ptr1.next) {
					int product = (ptr1.digit * ptr2.digit) + carryOver;
					carryOver = product / 10;
					Node.digit = product % 10;	
					if(ptr1.next != null) {
						Node.next = new DigitNode(0,null);
						Node = Node.next;
					}
					else {
						if(carryOver != 0)
							Node.next = new DigitNode(carryOver,null);
						break;
					}
				}
				bigInt = BigInteger.add(bigInt, nextNode);
			}
			if(first.negative == second.negative) {
				bigInt.negative = false;
			}
			else {
				bigInt.negative = true;
			}
			int count = 0;
			leadingZeros = 0;
			bigInt.numDigits = 0;
			for(DigitNode ptr = bigInt.front; ptr != null; ptr = ptr.next, count++) {
				if(ptr.digit != 0) {
					leadingZeros = count;
				}
			}
			count = 0;
			for(DigitNode ptr = bigInt.front; ptr != null; ptr = ptr.next, count++) {
				bigInt.numDigits++;
				if(count == leadingZeros) {
					ptr.next = null;
					break;
				}
			}
			if(bigInt.numDigits == 1 && bigInt.front.digit == 0 || bigInt.numDigits == 0) {
				bigInt.negative = false;
				bigInt.front = null;
				bigInt.numDigits = 0;
			}
			return bigInt;
		}
	
		private void reverseList(){  
			DigitNode prev = null;
			DigitNode curr = front;
			while (curr != null) {
				DigitNode d = curr.next;
				curr.next = prev;
				prev = curr;
				curr = d;
			}
			front = prev;
		}
		
		private static boolean greaterThan(BigInteger first, BigInteger second) {
			if(first.numDigits > second.numDigits) {
				return false;
			}
			else if(second.numDigits > first.numDigits) {
				return true;
			}
			else {
			DigitNode ptr1 = first.front;
			DigitNode ptr2 = second.front;
			boolean greater = false;
			while(ptr1 != null && ptr2 != null) {
				if(ptr2.digit > ptr1.digit) {
					greater = true;
				} 
				else if(ptr2.digit < ptr1.digit) {
					greater = false;
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			return greater;
			}
		}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
