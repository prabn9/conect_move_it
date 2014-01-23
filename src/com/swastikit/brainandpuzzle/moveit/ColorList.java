package com.swastikit.brainandpuzzle.moveit;

import android.graphics.Color;

public class ColorList {

	static int[][] colorDot = new int[][]{
		{ 0xfff40a09, 0x77f40a09 }, 
		{ 0xffff8000, 0x77ff8000 }, 
		{ 0xff0000FD, 0x770000FD }, 
		{ 0xff800000, 0x77800000 }, 
		{ 0xffffff00, 0x77ffff00 }, 
		{ 0xfffe00ff, 0x77fe00ff }, 
		{ 0xff00fffc, 0x7700fffc }, 
//		{ 0xff7f017f, 0x777f017f }, 
//		{ 0xffffffff, 0x77ffffff }, 
		{ 0xff808080, 0x77808080 }, 
		{ 0xff008000, 0x77008000 }, 
		{ 0xff7f8000, 0x777f8000 }, 
		{ 0xff00007f, 0x7700007f }, 
		{ 0xff007f7e, 0x77007f7e }, 
		{ 0xff7fff00, 0x777fff00 }, 
		{ 0xffffff00, 0x77ffff00 }, 
		{ 0xffff0080, 0x77ff0080 }, 
		{ 0xffcc99ff, 0x77cc99ff }, 
		{ 0xffcc9933, 0x77cc9933 },
		{ 0xff007ffe, 0x77007ffe },
		{ 0xff00fe7e, 0x7700fe7e },
		{ 0xff99ff99, 0x7799ff99 } 
    };
 

	
	public static int getColor(int row, int col) {
		return colorDot[row][col];

	}// end of setColor
}
