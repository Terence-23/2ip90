import java.util.Collections;
import java.util.stream.IntStream;
import java.util.Arrays;

class Poker {
    enum Suit {
        H, D, C, S
    }

    class Card implements Comparable<Card> {
        int rank;
        Suit suit;

        Card(String str) {
            int c = str.charAt(1);
            switch (c) {
                case 'H':
                    this.suit = Suit.H;
                    break;
                case 'D':
                    this.suit = Suit.D;
                    break;
                case 'C':
                    this.suit = Suit.C;
                    break;
                case 'S':
                    this.suit = Suit.S;
                    break;
            }
            c = str.charAt(0);
            switch (c) {
                case 'T':
                    this.rank = 10;
                    break;
                case 'J':
                    this.rank = 11;
                    break;
                case 'Q':
                    this.rank = 12;
                    break;
                case 'K':
                    this.rank = 13;
                    break;
                case 'A':
                    this.rank = 14;
                    break;
                default:
                    this.rank = Integer.parseInt(Character.toString(c));
            }
        }

        @Override
        public int compareTo(Card o) {
            return this.rank - o.rank;
        }

    }

    enum HandType {
        High,
        Pair,
        TwoPair,
        Three,
        Straight,
        Flush,
        Full,
        Quad,
        StraightFlush,
    }

    class Hand implements Comparable<Hand> {
        Card[] cards;
        HandType hType;
        Card[] important_cards;

        @Override
        // left > right
        public int compareTo(Hand h) {
            if (h.hType == hType) {
                if (h) {

                    return cards[4].compareTo(h.cards[4]);
                }

            }
            return hType.compareTo(h.hType);
        }

        Hand(Card[] cards) {
            Arrays.sort(cards);
            if (isStraightFlush(cards)) {

            }

        }

        static Integer[] isStraightFlush(Card[] cards) {
            if (isStraight(cards) != null && isFlush(cards) != null) {
                return IntStream.range(0, cards.length)
                        .mapToObj(i -> cards[cards.length - 1 - i]).toArray(Integer[]::new);
            }
            return null;
        }

        static Integer[] isFlush(Card[] cards) {
            Suit s = cards[0].suit;
            for (int i = 0; i < cards.length; ++i) {
                if (s != cards[i].suit) {
                    return null;
                }
            }
            return cards.stream().mapToObj(c -> c.rank).toArray(Integer[]::new);
        }

        static Integer[] isQuad(Card[] cards) {
            int rank = cards[1].rank;
            if (IntStream.range(0, 4).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank)
                    || IntStream.range(1, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank)) {
                return new Integer[] { rank, cards[4].rank };
            }
            return null;
        }

        static Integer[] isFull(Card[] cards) {
            int rank1 = cards[0].rank;
            int rank2 = cards[4].rank;
            if (IntStream.range(0, 3).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)
                    && IntStream.range(3, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank1, rank2 };
            } else if (IntStream.range(0, 2).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)
                    && IntStream.range(2, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank2, rank1 };
            }
            return null;
        }

        static Integer[] isStraight(Card[] cards) {
            int last_rank = cards[0].rank;
            for (int i = 1; i < 4; ++i) {
                if (last_rank != cards[i].rank - 1) {
                    return null;
                }
                last_rank = cards[i].rank;
            }
            if (cards[4].rank == 14 && last_rank == 4 || last_rank == cards[4].rank - 1) {
                return IntStream.range(0, cards.length)
                        .mapToObj(i -> cards[cards.length - 1 - i]).toArray(Integer[]::new);
            }
            return null;
        }

        static Integer[] isThree(Card[] cards) {
            int rank1 = cards[2].rank;
            if (IntStream.range(0, 3).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)) {
                return new Integer[] { rank1, cards[4].rank, cards[5].rank };
            }
            if (IntStream.range(1, 4).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)) {
                return new Integer[] { rank1, cards[0].rank, cards[5].rank };
            }
            if (IntStream.range(2, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)) {
                return new Integer[] { rank1, cards[0].rank, cards[1].rank };
            }
            return null;
        }

        static Integer[] isTwoPair(Card[] cards) {

            int rank1 = cards[1].rank;
            int rank2 = cards[3].rank;

            if (IntStream.range(0, 2).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)
                    && IntStream.range(2, 4).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank2, rank1, cards[5].rank };
            }
            if (IntStream.range(0, 2).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)
                    && IntStream.range(3, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank2, rank1, cards[2].rank };
            }
            if (IntStream.range(1, 3).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)
                    && IntStream.range(3, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank2, rank1, cards[0].rank };
            }
            return null;
        }

        static Integer[] isPair(Card[] cards) {
            int rank1 = cards[1].rank;
            if (IntStream.range(0, 2).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)) {
                return new Integer[] { rank1, cards[4].rank, cards[3].rank, cards[2].rank };
            }
            if (IntStream.range(1, 3).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank1)) {
                return new Integer[] { rank1, cards[4].rank, cards[3].rank, cards[0].rank };
            }
            int rank2 = cards[3].rank;
            if (IntStream.range(2, 4).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank1, cards[4].rank, cards[1].rank, cards[0].rank };
            }
            if (IntStream.range(3, 5).mapToObj(i -> cards[i]).allMatch(c -> c.rank == rank2)) {
                return new Integer[] { rank1, cards[2].rank, cards[1].rank, cards[0].rank };
            }
            return null;
        }

    }

}
