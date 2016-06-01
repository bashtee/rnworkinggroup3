package irc.classes;

import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ArrayUtils;

import irc.enums.FieldType;
import irc.enums.MessageType;
import irc.interfaces.*;


/**
 * @author mader
 * Factory Class
 * */
public final class Values {
	
	//Constructor
	private Values(){}
	
	/**
	 * @author mader
	 * Function to create a new instance of a server
	 * @param port Port on which the Server should listen
	 * @return New instance of ISimpleServer
	 * */
	public static ISimpleServer createNewServer(int port,Queue<ISimpleDataInput> pendingMessages, ReentrantLock lock, Condition cond){
		return Server.valueOf(port,pendingMessages, lock, cond);
	}
	
	/**
	 * @author mader
	 * Function to create a new instance of a client
	 * @return New instance of ISimpleClient
	 * */
	public static ISimpleClient createNewClient(Queue<ISimpleMessage> massagePool,ReentrantLock lock, Condition cond){
		return Client.valueOf(massagePool, lock, cond);
	}
	
	/**
	 * @author mader
	 * Function to create a new instance of a IRC-User
	 * @return new instance of IsimpleUser
	 * */
	public static ISimpleUser createNewUser(String ip,int port){
		return User.valueOf(ip,port);
	}
	
	/**
	 * @author mader
	 * Function to create a new chat
	 * @return a new instance of ISimpleChat
	 * @param userList List of Users for the Chat
	 * */
	public static ISimpleChat createNewChat(List<ISimpleUser> userList){
		return Chat.valueOf(userList);
	}
	
	/**
	 * @author mader
	 * Function to create a new Message
	 * @param header Header of the message
	 * @param recipients user who should get the Message
	 * @param message Message to be send
	 * @return ISimpleMessage new instance of ISimpleMessage
	 * */
	public static ISimpleMessage createNewMessage(ISimpleHeader header,List<ISimpleUser> recipients ,List<ISimpleContent> contentList){
		return Message.valueOf(header,recipients,contentList);
	}
	
	/**
	 * @author mader
	 * Function to create a new Instace of a header
	 * @param sender Sender of the Message
	 * @param type type of the Message (not null)
	 * @param numOfField Number of Field from the Message
	 * @param version Version of the protocol
	 * @return ISimpleHeader new instance of ISimpleHeader
	 * */
	public static ISimpleHeader createNewHeader(ISimpleUser sender, MessageType type, int version) throws IllegalArgumentException{
		return Header.valueOf(version, type, sender);
	}
	
	/**
	 * @author mader
	 * Function to Create a Header out of bytes.
	 * @param bytes bytes which represent a Header
	 * @return ISimpleHeader new instance of ISimpleHeader
	 * */
	public static ISimpleHeader createNewHeader(byte[] bytes){
		return Header.createFromBytes(bytes);
	}

	/**
	 * Function to create a new Instance of a Worker
	 * @author mader
	 * @param pendingMessages Queue with raw messages
	 * @param chatList List with Chats
	 * @param userList List of user from this Server
	 * @param lock Lock for access to the Queue
	 * @param cond Condition to wait till something can be done
	 * @return new Instance of IWorker
	 * */
	public static IWorker createNewWorker(Queue<ISimpleDataInput> pendingMessages, Lock lock, Condition cond, IServerController serverCtrl) {
		return Worker.valueOf(pendingMessages,lock,cond,serverCtrl);
	}
	
	/**
	 * Function to create a ContentType
	 * @author mader
	 * @param type FieldType of the Content to determine the ISimpleContent
	 * @param content content of the Field as bytearray
	 * @return new instance of ISimpleContent
	 * 
	 * */
	public static ISimpleContent createNewContent(byte[] content){
		if(!(content.length > IRCUtils.FIELD_HEADER_BYTE_SIZE-1)){
			return null;
		}
		int length = IRCUtils.convertByteToInt(content[2], content[3]);
		if(!(length <= content.length - IRCUtils.FIELD_HEADER_BYTE_SIZE)){
			return null;
		}
		FieldType type = FieldType.valueOf(ArrayUtils.subarray(content, 0, IRCUtils.FIELDTYPE_LENGTH));
		
		if(type == FieldType.IP){
			return createNewContentIP(ArrayUtils.subarray(content,IRCUtils.FIELD_HEADER_BYTE_SIZE,content.length),length);
		}else if(type == FieldType.NAME){
			return createNewContentName(ArrayUtils.subarray(content,IRCUtils.FIELD_HEADER_BYTE_SIZE,content.length),length);
		} else if(type == FieldType.PORT){
			return createNewContentPort(ArrayUtils.subarray(content,IRCUtils.FIELD_HEADER_BYTE_SIZE,content.length),length);
		} else if(type == FieldType.TEXT){
			return createNewContentText(ArrayUtils.subarray(content,IRCUtils.FIELD_HEADER_BYTE_SIZE,content.length),length);
		} else if(type == FieldType.USER_LIST){
			return createNewContentUserList(ArrayUtils.subarray(content,IRCUtils.FIELD_HEADER_BYTE_SIZE,content.length),length);
		} else 
			return null;
	}
	
	/**
	 * Function to create a new instance of ContentIP
	 * @author mader
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentIP(String ip){
		return ContentIP.valueOf(ip);
	}
	
	/**
	 * Function to create a new instance of ContentName
	 * @author mader
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentName(String name){
		return ContentName.valueOf(name);
	}
	
	/**
	 * Function to create a new instance of ContentPort
	 * @author mader
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentPort(int port){
		return ContentPort.valueOf(port);
	}
	
	/**
	 * Function to create a new instance of ContentText
	 * @author mader
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentText(String message){
		return ContentText.valueOf(message);
	}
	
	/**
	 * Function to create a new instance of ContentUserList
	 * @author mader
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContentUserList createNewContentUserList(List<ISimpleUser> userList){
		return ContentUserList.valueOf(userList);
	}
	
	/**
	 * Function to create a new instance of ContentIP
	 * @author mader
	 * @param content Content as bytearray
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentIP(byte[] content,int length){
		return ContentIP.valueOf(content,length);
	}
	
	/**
	 * Function to create a new instance of ContentName
	 * @author mader
	 * @param content Content as bytearray
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentName(byte[] content,int length){
		return ContentName.valueOf(content,length);
	}
	
	/**
	 * Function to create a new instance of ContentPort
	 * @author mader
	 * @param content Content as bytearray
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentPort(byte[] content,int length){
		return ContentPort.valueOf(content,length);
	}
	
	/**
	 * Function to create a new instance of ContentText
	 * @author mader
	 * @param content Content as bytearray
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContent createNewContentText(byte[] content,int length){
		return ContentText.valueOf(content,length);
	}
	
	/**
	 * Function to create a new instance of ContentUserList
	 * @author mader
	 * @param content Content as bytearray
	 * @return new instance of ISimpleContent
	 * */
	public static ISimpleContentUserList createNewContentUserList(byte[] content,int length){
		return ContentUserList.valueOf(content,length);
	}
	
	/**
	 * Function to create a new instance of ISImpleDataInput
	 * */
	public static ISimpleDataInput createNewDataInput(Socket s){
		return DataInput.valueOf(s);
	}

}
