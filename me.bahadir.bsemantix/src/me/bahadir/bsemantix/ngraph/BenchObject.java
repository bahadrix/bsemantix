package me.bahadir.bsemantix.ngraph;

import java.io.Serializable;
import java.util.List;

import javax.media.j3d.TransformGroup;

import me.bahadir.bsemantix.parts.TreePair;



public interface BenchObject extends Serializable{

	public void onClick();
	public void onUnClick();
	public void onHover();
	public void onHoverExit();
	public void onHoverIn();
	public void onTransformChange(TransformGroup group);
	public void setTransparency(float alpha);
	public void disable();
	public void enable();
	public List<TreePair> getSpecPairs();
}
