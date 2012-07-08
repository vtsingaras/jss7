package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProtocolId;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class ExternalSignalInfoImpl implements ExternalSignalInfo, MAPAsnPrimitive {
	private byte[] signalInfo = null;
	private ProtocolId protocolId = null;
	private MAPExtensionContainer extensionContainer = null;
	
	private static final String _PrimitiveName = "ExternalSignalInfo";
	
	
	public ExternalSignalInfoImpl() {}

	public ExternalSignalInfoImpl(byte[] signalInfo, ProtocolId protocolId, 
								  MAPExtensionContainer extensionContainer) {
		this.signalInfo = signalInfo;
		this.protocolId = protocolId;
		this.extensionContainer = extensionContainer;
	}

	@Override
	public byte[] getSignalInfo() {
		return this.signalInfo;
	}

	@Override
	public ProtocolId getProtocolId() {
		return this.protocolId;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}
	
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.signalInfo = null;
		this.protocolId = null;
		this.extensionContainer = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
				switch (tag) { 
				case Tag.ENUMERATED: 
					int code = (int) ais.readInteger();
					this.protocolId = ProtocolId.getProtocolId(code);
					break;
				case Tag.STRING_OCTET: 
					this.signalInfo = ais.readOctetString();
					break;
				case Tag.SEQUENCE:
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".extensionContainer: Parameter extensionContainer is primitive", 
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
					break;
				default:
					ais.advanceElement();
					break;
				}
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			if(this.protocolId != null)
		 	  asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.protocolId.getCode());
			if(this.signalInfo != null)
			  asnOs.writeOctetString(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, this.signalInfo);
			if(this.extensionContainer != null)
			  ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding ExternalSignalInfo : " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding ExternalSignalInfo : " + e.getMessage(), e);
		}
	}
}