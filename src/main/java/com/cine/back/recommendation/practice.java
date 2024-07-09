// package com.cine.back.recommendation;

// import java.util.HashSet;
// import java.util.Scanner;

// public class practice {

//     Scanner sc = new Scanner(System.in);
// 		HashSet<String> hs01 = new HashSet<>();
// 		HashSet<String> hs02 = new HashSet<>();
// 		HashSet<String> hsSum = new HashSet<>();
		
// 		hs01.add("1");
// 		hs01.add("2");
// 		hs01.add("3");
// 		hs01.add("4");
		
// 		hs02.add("1");
// 		hs02.add("2");
		
// 		//합집합 버퍼의 기준 정하기
// 		hsSum = (HashSet)hs01.clone();
		
// 		int common = 0;
		
// 		//합집합 만들기 & 교집합 갯수 구하기
// 		for(String hs2Element: hs02)
// 			if(hs01.contains(hs2Element)) common++;
// 			else hsSum.add(hs2Element);
// 		//결과값 출력
// 		System.out.println((double)common/hsSum.size());
//                                     2    /   4
//                     * 교집합
//                             hs01(1), hs01(2)
//                             hs02(1), hs02(2)    교집합 = 2개

//                     * 합집합
//                             hs01(4) + hs02(2) - 2개(교집합) = 4개 
//                     결과
//                             2 / 4 = 0.5  50% 유사하다고 결론.
//                 ---------------------------------------------------
//                 * 유저 A의 찜목록    


//                 * 유저 B의 찜목록    
                
// }
