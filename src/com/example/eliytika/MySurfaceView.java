package com.example.eliytika;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

@SuppressLint("ClickableViewAccessibility")
public class MySurfaceView extends SurfaceView implements Runnable,SurfaceHolder.Callback{
	SurfaceHolder holder;
	Thread MainLoop;

	Drawable img; //画像の表示準備
	Bitmap ziki,tama,teki; //画像の表示準備（こっちの方が複雑


	boolean tama_active=false;
	int x,y,disp_w,disp_h,tama_x,tama_y,teki_x,teki_y;
	private SoundPool sp;
	int sound;
	int i ,tama_nam = 120;

	int[] shot_x= new int[tama_nam+10];
	int[] shot_y= new int[tama_nam+10];
	int[] shot_flg= new int[tama_nam+10];



	/*
	 *
	 */


	//コンストラクタ（setContentView）
	public MySurfaceView(Context context) {
		super(context);
		init(context);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	//コンストラクタ（main.xml）
	public MySurfaceView(Context context,AttributeSet attrs){
		super(context);
		init(context);
	}

	/*
	 *
	 */



	public void init(Context context){
		holder=getHolder();
		holder.addCallback(this);
		setFocusable(true);
		requestFocus();

		//デバイスの解像度取得
		WindowManager windowmanager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		// ディスプレイのインスタンス生成
		Display disp = windowmanager.getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		disp_w = size.x;
		disp_h = size.y;


		//使用する画像を指定
		Resources res = context.getResources();
		ziki = BitmapFactory.decodeResource(res, R.drawable.player);
		tama = BitmapFactory.decodeResource(res, R.drawable.bullet_player);
		teki = BitmapFactory.decodeResource(res, R.drawable.enemy_00);

		holder.setFixedSize(800, 1200);		//端末に合わせた解像度
		x = 390; y = 1000;
		teki_x = 390; teki_y = 100;

		//サウンド
		sp=new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		sound=sp.load(context, R.raw.player_bullet, 0);

	}


	//描画
		private void draw() {
			// TODO 自動生成されたメソッド・スタブ

			Canvas c=holder.lockCanvas();


			if(c!=null){
				//背景の色を設定
				c.drawColor(Color.BLACK);

			//弾の発射

				// 画面上にでていない弾があるか、弾の数だけ繰り返して調べる
				for( i = 0 ; i < tama_nam ; i ++ )
				{
					// 弾iが画面上にでていない場合はその弾を画面に出す
					if( shot_flg[i] == 0 )
					{
						shot_x[i]=x;
						shot_y[i]=y;

						// 弾iは現時点を持って存在するので、存在状態を保持する変数に１を代入する
						shot_flg[i] = 1 ;

						// 一つ弾を出したので弾を出すループから抜けます
						break ;
					}
				}

				// 弾の数だけ弾を動かす処理を繰り返す
				for(i = 0 ; i < tama_nam ; i ++ )
				{
					// 自機の弾iの移動ルーチン( 存在状態を保持している変数の内容が１(存在する)の場合のみ行う )
					if( shot_flg[ i ] == 1 )
					{
						// 弾iを１６ドット上に移動させる
						shot_y[ i ] -= 16 ;

						// 画面外に出てしまった場合は存在状態を保持している変数に０(存在しない)を代入する
						if( shot_y[ i ] < -800 )
						{
							    shot_flg[i] = 0;

						}
						if(i % 5 == 0 /*&& tama_active*/){
						c.drawBitmap(tama, shot_x[i]+10, shot_y[i], null);


						}



					}
				}

				/*****
				if(tama_active){
					c.drawBitmap(tama, tama_x, tama_y, null);
					tama_y -= 5;
					if(tama_y<-50)tama_active=false;
				}
				******/


				//自機(画像)の表示
				c.drawBitmap(ziki, x, y, null);
				c.drawBitmap(teki, teki_x, teki_y, null);

			}


			holder.unlockCanvasAndPost(c);

		}



	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO 自動生成されたメソッド・スタブ
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO 自動生成されたメソッド・スタブ
		//スレッドの作成
		MainLoop=new Thread(this);
		//スレッドの開始
		MainLoop.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO 自動生成されたメソッド・スタブ
		Thread.interrupted();
	}


	//ここで描画を繰り返す
	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		for( i = 0 ; i < tama_nam ; i ++ )
		{
			shot_flg[i] = 0 ;
		}

		//無限ループ
		while(true){

			//再描画処理用関数
			draw();
		}

	}




	int x1,y1,x2,y2;

	public boolean onTouchEvent(MotionEvent event){


		int n=event.getAction();
		try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		switch(n){


		case MotionEvent.ACTION_DOWN:
			x1=(int)event.getX()*800/disp_w;
			y1=(int)event.getY()*1200/disp_h;
			sp.play(sound,1.0f, 1.0f, 1, 0, 1.0f);

			/*for( i = 0 ; i < tama_nam ; i ++ )
			{
				if( shot_flg[ i ] == 1 )
				{
				shot_flg[i] = 0 ;

				}
			}*/
			//tama_active = true;
			break;


		case MotionEvent.ACTION_MOVE:
			x2=(int)event.getX()*800/disp_w;
			y2=(int)event.getY()*1200/disp_h;
			x=x+(x2-x1);
			if(x>700){		x=700;
			}else if(x<0){	x=0;}
			y=y+(y2-y1);
			if(y>1100){		y=1100;
			}else if(x<0){	y=0;}
			x1=x2;y1=y2;
			//tama_active = true;

		/*	if( shot_flg[ i ] == 1 ){
				tama_x=x;tama_y=y;
				sp.play(sound,1.0f, 1.0f, 1, 0, 1.0f);

				}:*/

			break;


			}

		return true;

	}

}

