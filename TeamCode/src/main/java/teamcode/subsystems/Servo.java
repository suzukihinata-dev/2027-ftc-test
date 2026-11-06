package teamcode.subsystems;

import ftclib.motor.FtcServo;
import trclib.motor.TrcServo;
import trclib.robotcore.TrcEvent;
import trclib.subsystem.TrcSubsystem;

//クラス宣言
public class Servo extends TrcSubsystem<Servo.Action>
{
    public enum Action
    {
        getPointFlower, //フラワーで得点する
        intakeElevator, //エレベーターを格納する
        MoveUp,         //dpad上: 押している間サーボを上げる
        MoveDown,       //dpad下: 押している間サーボを下げる
        Stop,           //ボタンを離したらその場で止める
    }

    public static final String SUBSYSTEM_NAME = "Servo";
    private static final String SERVO_NAME = "servo";
    // サーボの可動範囲 (0.0 ~ 1.0 がフルレンジ)
    private static final double MIN_POS = 0.0;
    private static final double MAX_POS = 1.0;
    private static final double INIT_POS = 0.5;
    // フラワー得点位置 / エレベーター格納位置
    private static final double FLOWER_POS = MAX_POS;
    private static final double INTAKE_POS = MIN_POS;
    // ボタン長押し時の移動速度 (位置/秒)
    private static final double MAX_STEP_RATE = 0.5;
    private static final double MOVE_POWER = 1.0;

    private final FtcServo servo;

    public Servo()
    {
        super(SUBSYSTEM_NAME, false);
        TrcServo.Params params = new TrcServo.Params().setMaxStepRate(MAX_STEP_RATE);
        servo = new FtcServo(SERVO_NAME, params);
        servo.setInverted(true); //逆回転設定をon
    }

    @Override
    public void cancel()
    {
        //動作中のサーボの処理をキャンセルする
        servo.cancel();
    }

    @Override
    public void zeroCalibrate(String owner, TrcEvent completionEvent)
    {
        //TODO: 必要になったらキャリブレーション基準を作るプログラムを作成
    }

    @Override
    public void resetState()
    {
        //サーボの出力を0に設定し、停止状態にする
        servo.setPower(0.0);
    }

    @Override
    public void subsystemControl(boolean altFunc, double... inputs)
    {
        ensureInitialPosition();
        servo.setPower(inputs[0], MIN_POS, MAX_POS);
    }

    @Override
    public void subsystemAction(Action action, Object context)
    {
        switch (action)
        {
            case getPointFlower:
                servo.setPosition(FLOWER_POS);
                break;
            case intakeElevator:
                servo.setPosition(INTAKE_POS);
                break;
            case MoveUp:
                ensureInitialPosition();
                servo.setPower(MOVE_POWER, MIN_POS, MAX_POS);
                break;
            case MoveDown:
                ensureInitialPosition();
                servo.setPower(-MOVE_POWER, MIN_POS, MAX_POS);
                break;
            case Stop:
            default:
                servo.setPower(0.0);
                break;
        }
    }

    /**
     * まだ位置指令が出ていない(NaN)場合、初期位置に設定する。
     */
    private void ensureInitialPosition()
    {
        if (Double.isNaN(servo.getPosition()))
        {
            servo.setPosition(INIT_POS);
        }
    }

    @Override
    public void tuneSubsystem(TuneAction action, String tuneSubsystemName)
    {
    }

    @Override
    public void publishToDashboard()
    {
    }

    @Override
    public int updateStatus(int lineNum, boolean slowLoop)
    {
        return lineNum;
    }

    @Override
    public void updateParamsToDashboard(String subsystemName)
    {
    }

    @Override
    public void updateParamsFromDashboard(String subsystemName)
    {
    }
}
