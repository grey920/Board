package net.board.action;

import javax.servlet.http.*;
/*
Ư�� ����Ͻ� ��û���� �����ϰ� ������� ActoinForward Ÿ������ ��ȯ�ϴ�
�޼��尡 ���ǵǾ� �ֽ��ϴ�.
Action : �������̽���
ActionForward : ��ȯ��
*/
public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) throws Exception;
}
