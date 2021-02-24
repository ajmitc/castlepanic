package castlepanic.game.monster;

public class Draw3MonsterTokens extends Monster{
    public Draw3MonsterTokens(){
        super(MonsterType.EFFECT, "Draw 3 Monster Tokens", 0, "Draw 3 Monster Tokens.png");
        abilities.add(MonsterAbility.DRAW_3_MONSTER_TOKENS);
    }
}
