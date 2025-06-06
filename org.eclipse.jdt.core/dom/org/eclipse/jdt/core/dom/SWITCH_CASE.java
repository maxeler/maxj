/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Switch case AST node type. A switch case is a special kind of node used only
 * in switch statements. It is a <code>Statement</code> in name only.
 * <pre>
 * SWITCHCASE:
 *		<b>case</b> Expression  <b>:</b>
 *		<b>default</b> <b>:</b>
 * </pre>
 *
 * @since 3.42
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
@SuppressWarnings("rawtypes")
public class SWITCH_CASE extends Statement {

	/**
	 * The "expression" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor EXPRESSION_PROPERTY =
		new ChildPropertyDescriptor(SWITCH_CASE.class, "expression", Expression.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List propertyList = new ArrayList(2);
		createPropertyList(SWITCH_CASE.class, propertyList);
		addProperty(EXPRESSION_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants
	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}

	/**
	 * The expression; <code>null</code> for none; lazily initialized (but
	 * does <b>not</b> default to none).
	 * @see #expressionInitialized
	 */
	private Expression optionalExpression = null;

	/**
	 * Indicates whether <code>optionalExpression</code> has been initialized.
	 */
	private boolean expressionInitialized = false;

	/**
	 * Creates a new AST node for a switch case pseudo-statement owned by the
	 * given AST. By default, there is an unspecified, but legal, expression.
	 *
	 * @param ast the AST that is to own this node
	 */
	SWITCH_CASE(AST ast) {
		super(ast);
	}

	@Override
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	@Override
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == EXPRESSION_PROPERTY) {
			if (get) {
				return getExpression();
			} else {
				setExpression((Expression) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	@Override
	final int getNodeType0() {
		return _SWITCH_CASE;
	}

	@Override
	ASTNode clone0(AST target) {
		SWITCH_CASE result = new SWITCH_CASE(target);
		result.setSourceRange(getStartPosition(), getLength());
		result.copyLeadingComment(this);
		result.setExpression(
			(Expression) ASTNode.copySubtree(target, getExpression()));
		return result;
	}

	@Override
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	@Override
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			acceptChild(visitor, getExpression());
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the expression of this switch case, or
	 * <code>null</code> if there is none (the "default:" case).
	 *
	 * @return the expression node, or <code>null</code> if there is none
	 */
	public Expression getExpression() {
		if (!this.expressionInitialized) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (!this.expressionInitialized) {
					preLazyInit();
					this.optionalExpression = postLazyInit(new SimpleName(this.ast), EXPRESSION_PROPERTY);
					this.expressionInitialized = true;
				}
			}
		}
		return this.optionalExpression;
	}

	/**
	 * Sets the expression of this switch case, or clears it (turns it into
	 * the  "default:" case).
	 *
	 * @param expression the expression node, or <code>null</code> to
	 *    turn it into the  "default:" case
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setExpression(Expression expression) {
		ASTNode oldChild = this.optionalExpression;
		preReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
		this.optionalExpression = expression;
		this.expressionInitialized = true;
		postReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
	}

	/**
	 * Returns whether this switch case represents the "default:" case.
	 * <p>
	 * This convenience method is equivalent to
	 * <code>getExpression() == null</code>.
	 * </p>
	 *
	 * @return <code>true</code> if this is the default switch case, and
	 *    <code>false</code> if this is a non-default switch case
	 */
	public boolean isDefault()  {
		return getExpression() == null;
	}

	@Override
	int memSize() {
		return super.memSize() + 2 * 4;
	}

	@Override
	int treeSize() {
		return
			memSize()
			+ (this.optionalExpression == null ? 0 : this.optionalExpression.treeSize());
	}
}
