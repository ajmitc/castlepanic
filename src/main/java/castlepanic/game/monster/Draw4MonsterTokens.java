package castlepanic.game.monster;

public class Draw4MonsterTokens extends Monster{
    public Draw4MonsterTokens(){
        super(MonsterType.EFFECT, "Draw 4 Monster Tokens", 0, "Draw 4 Monster Tokens.png");
        abilities.add(MonsterAbility.DRAW_4_MONSTER_TOKENS);
    }
}
