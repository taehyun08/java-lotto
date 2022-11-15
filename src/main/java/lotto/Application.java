package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        String input;
        List<Integer> winningNums = new ArrayList<>();
        int buyMoney = 0;
        int lottoNum = 0;
        int bonusNum = 0;
        long winningMoney = 0;
        List<Lotto> lottos = new ArrayList<>();
        System.out.println("구입금액을 입력해 주세요.");
        input = Console.readLine();
        isInt(input);
        buyMoney = Integer.parseInt(input);
        checkPayMoney(buyMoney);
        System.out.println(buyMoney/1000 + "개를 구매했습니다.");
        lottoNum = checkPayMoney(buyMoney);
        lottos = makeLottoNumber(lottos, lottoNum);
        printLottos(lottos);
        System.out.println("당첨 번호를 입력해 주세요.");
        input = Console.readLine();
        String[] inputs = input.split(",");
        winningNums = stringToIntList(inputs);
        Lotto winningLotto = new Lotto(winningNums);
        System.out.println("보너스 번호를 입력해 주세요.");
        input = Console.readLine();
        bonusNum = Integer.parseInt(input);
        System.out.println("당첨통계\n---");
        printWinning(lottos, winningLotto, bonusNum);
        long revenue = revenue(lottos, winningLotto, bonusNum);
        revenuePrint(buyMoney, revenue);
    }

    // num개의 lotto를 발행하는 함수
    public static List<Lotto> makeLottoNumber(List<Lotto> lottos, int num){
        for(int i = 0; i < num; i++) {
            List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            Lotto lotto = new Lotto(numbers);
            lottos.add(lotto);
        }
        return lottos;
    }

    public static int checkPayMoney(int money){
        if(money%1000 != 0){
            throw new IllegalArgumentException("[ERROR] 1000원 단위의 금액만 가능합니다.");
        }
        return money/1000;
    }

    public static int checkLottoSame(Lotto winningLotto, Lotto lotto){
        int same = 0;
        List<Integer> winNum = winningLotto.getNumbers();
        List<Integer> lottoNumbers = lotto.getNumbers();
        for(int i : lottoNumbers){
            if(winNum.contains(i)){
                same++;
            }
        }
        return same;
    }

    public static void printLottos(List<Lotto> lottos){
        for(Lotto lotto : lottos){
            List<Integer> numbers = new ArrayList<>(lotto.getNumbers());
            Collections.sort(numbers);
            System.out.println(numbers.toString());
        }
    }

    // string배열을 int List로 변환하는 함수
    public static List<Integer> stringToIntList(String[] str){
        List<Integer> nums = new ArrayList<>();
        int[] numbers = Arrays.stream(str).mapToInt(Integer::parseInt).toArray();
        for(int i : numbers){
            nums.add(i);
        }
        return nums;
    }

    public static void revenuePrint(long useMoney, long revenue){
        double revPercent = revenue/(double)useMoney * 100;
        revPercent = Math.round(revPercent*100)/100.0;
        System.out.println("총 수익률은 " + revPercent + "%입니다.");
    }

    public static int lottoRank(Lotto lotto,Lotto winningLotto, int bonus){
        int same = checkLottoSame(winningLotto, lotto);
        if(same == 5){
            List<Integer> nums = lotto.getNumbers();
            if(nums.contains(bonus)){
                return 2;
            }
            return 3;
        }
        if(same == 6){
            return 1;
        }
        return 8 - same;
    }

    public static long revenue(List<Lotto> lottos, Lotto winningLotto, int bonus){
        long revenue = 0;
        for(Lotto lotto : lottos){
            int rank = lottoRank(lotto, winningLotto, bonus);
            if(rank > 5){
                continue;
            }
            Rank[] values = Rank.values();
            revenue += values[rank - 1].getMoney();
        }
        return revenue;
    }

    public static void printWinning(List<Lotto> lottos, Lotto winningLotto, int bonus){
        int[] win = {0,0,0,0,0};
        for(Lotto lotto : lottos){
            int rank = lottoRank(lotto, winningLotto, bonus);
            if(rank > 5){
                continue;
            }
            win[rank-1]++;
        }
        for(int i = 0; i < 5; i++){
            Rank[] values = Rank.values();
            System.out.println(values[4-i].getPrinting() + " - " + win[4-i] + "개");
        }
    }

    public static void isInt(String str){
        try{
            int x = Integer.parseInt(str);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("[ERROR] 구매 금액은 숫자만 가능합니다.");
        }
    }
}