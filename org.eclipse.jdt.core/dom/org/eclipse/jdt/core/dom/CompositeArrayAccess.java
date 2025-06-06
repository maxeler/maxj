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
 * Array access expression AST node type.
 *
 * <pre>
 * ArrayAccess:
 *    Expression <b>[</b> Expression <b>]</b>
 * </pre>
 *
 * @since 3.42
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
@SuppressWarnings("rawtypes")
public class CompositeArrayAccess extends Expression {

	/**
	 * The "array" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor COMPOSITE_ARRAY_PROPERTY =
		new ChildPropertyDescriptor(CompositeArrayAccess.class, "array", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "index" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor INDEX_ONE_PROPERTY =
		new ChildPropertyDescriptor(CompositeArrayAccess.class, "indexOne", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	public static final ChildPropertyDescriptor INDEX_TWO_PROPERTY =
		new ChildPropertyDescriptor(CompositeArrayAccess.class, "indexTwo", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List properyList = new ArrayList(3);
		createPropertyList(CompositeArrayAccess.class, properyList);
		addProperty(COMPOSITE_ARRAY_PROPERTY, properyList);
		addProperty(INDEX_ONE_PROPERTY, properyList);
		addProperty(INDEX_TWO_PROPERTY, properyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(properyList);
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
	 * The array expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
	private volatile Expression arrayExpression;

	/**
	 * The index expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
	private volatile Expression indexExpressionOne;
	private volatile Expression indexExpressionTwo;

	/**
	 * Creates a new unparented array access expression node owned by the given
	 * AST. By default, the array and index expresssions are unspecified,
	 * but legal.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 *
	 * @param ast the AST that is to own this node
	 */
	CompositeArrayAccess(AST ast) {
		super(ast);
	}

	@Override
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	@Override
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == COMPOSITE_ARRAY_PROPERTY) {
			if (get) {
				return getArray();
			} else {
				setArray((Expression) child);
				return null;
			}
		}
		if (property == INDEX_ONE_PROPERTY) {
			if (get) {
				return getIndexOne();
			} else {
				setIndexOne((Expression) child);
				return null;
			}
		}

		if (property == INDEX_TWO_PROPERTY) {
			if (get) {
				return getIndexTwo();
			} else {
				setIndexTwo((Expression) child);
				return null;
			}
		}

		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	@Override
	final int getNodeType0() {
		return ARRAY_ACCESS;
	}

	@Override
	ASTNode clone0(AST target) {
		CompositeArrayAccess result = new CompositeArrayAccess(target);
		result.setSourceRange(getStartPosition(), getLength());
		result.setArray((Expression) getArray().clone(target));
		result.setIndexOne((Expression) getIndexOne().clone(target));
		result.setIndexTwo((Expression) getIndexTwo().clone(target));
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
			// visit children in normal left to right reading order
			acceptChild(visitor, getArray());
			acceptChild(visitor, getIndexOne());
			acceptChild(visitor, getIndexTwo());
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the array expression of this array access expression.
	 *
	 * @return the array expression node
	 */
	public Expression getArray() {
		if (this.arrayExpression == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.arrayExpression == null) {
					preLazyInit();
					this.arrayExpression = postLazyInit(new SimpleName(this.ast), COMPOSITE_ARRAY_PROPERTY);
				}
			}
		}
		return this.arrayExpression;
	}

	/**
	 * Sets the array expression of this array access expression.
	 *
	 * @param expression the array expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setArray(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		// an ArrayAccess may occur inside an Expression
		// must check cycles
		ASTNode oldChild = this.arrayExpression;
		preReplaceChild(oldChild, expression, COMPOSITE_ARRAY_PROPERTY);
		this.arrayExpression = expression;
		postReplaceChild(oldChild, expression, COMPOSITE_ARRAY_PROPERTY);
	}

	/**
	 * Returns the index expression of this array access expression.
	 *
	 * @return the index expression node
	 */
	public Expression getIndexOne() {
		if (this.indexExpressionOne == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.indexExpressionOne == null) {
					preLazyInit();
					this.indexExpressionOne = postLazyInit(new SimpleName(this.ast), INDEX_ONE_PROPERTY);
				}
			}
		}
		return this.indexExpressionOne;
	}

	public Expression getIndexTwo() {
		if (this.indexExpressionTwo == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.indexExpressionTwo == null) {
					preLazyInit();
					this.indexExpressionTwo = postLazyInit(new SimpleName(this.ast), INDEX_TWO_PROPERTY);
				}
			}
		}
		return this.indexExpressionTwo;
	}

	/**
	 * Sets the index expression of this array access expression.
	 *
	 * @param expression the index expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setIndexOne(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		// an ArrayAccess may occur inside an Expression
		// must check cycles
		ASTNode oldChild = this.indexExpressionOne;
		preReplaceChild(oldChild, expression, INDEX_ONE_PROPERTY);
		this.indexExpressionOne = expression;
		postReplaceChild(oldChild, expression, INDEX_ONE_PROPERTY);
	}

	public void setIndexTwo(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		// an ArrayAccess may occur inside an Expression
		// must check cycles
		ASTNode oldChild = this.indexExpressionTwo;
		preReplaceChild(oldChild, expression, INDEX_TWO_PROPERTY);
		this.indexExpressionTwo = expression;
		postReplaceChild(oldChild, expression, INDEX_TWO_PROPERTY);
	}

	@Override
	int memSize() {
		return BASE_NODE_SIZE + 2 * 4;
	}

	@Override
	int treeSize() {
		return
			memSize()
			+ (this.arrayExpression == null ? 0 : getArray().treeSize())
			+ (this.indexExpressionOne == null ? 0 : getIndexOne().treeSize())
			+ (this.indexExpressionTwo == null ? 0 : getIndexTwo().treeSize());
	}
}

