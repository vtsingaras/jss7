/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.isup.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public enum ReturnResultProblemType {

    /**
     * No operation with the specified invoke ID is in progress. This code is generated by the TCAP layer.
     */
    UnrecognizedInvokeID(0),

    /**
     * The invoked operation does not report success. This code is generated by the TCAP layer.
     */
    ReturnResultUnexpected(1),

    /**
     * Signifies that the type of parameter in the return result component is not that agreed by the two TC-Users. This code is
     * generated by the TC-User (not by TCAP layer).
     */
    MistypedParameter(2);

    ReturnResultProblemType(long l) {
        this.type = l;
    }

    private long type;

    /**
     * @return the type
     */
    public long getType() {
        return type;
    }

    public static ReturnResultProblemType getFromInt(long t) throws ParameterException {
        if (t == 0) {
            return UnrecognizedInvokeID;
        } else if (t == 1) {
            return ReturnResultUnexpected;
        } else if (t == 2) {
            return MistypedParameter;
        }
        throw new ParameterException( "Wrong value of type: " + t);
    }
}
