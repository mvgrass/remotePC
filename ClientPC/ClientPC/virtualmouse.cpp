#include "virtualmouse.h"
#include "windows.h"

VirtualMouse::VirtualMouse(QObject *parent) : QObject(parent)
{

}

void VirtualMouse::absoluteMove(qint32 x, qint32 y)
{
    MOUSEINPUT mINPUT;

    mINPUT.dx = x;
    mINPUT.dy = y;

    mINPUT.dwFlags = MOUSEEVENTF_MOVE|MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_VIRTUALDESK;

    mINPUT.mouseData = 0;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));

}

void VirtualMouse::move(qint32 dx, qint32 dy)
{
    MOUSEINPUT mINPUT;

    mINPUT.dx = dx;
    mINPUT.dy = dy;

    mINPUT.dwFlags = MOUSEEVENTF_MOVE;

    mINPUT.mouseData = 0;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));
}

void VirtualMouse::rightClick()
{
    rightButtonDown();
    rightButtonUp();

}

void VirtualMouse::rightButtonDown()
{
    MOUSEINPUT mINPUT;

    mINPUT.dwFlags = MOUSEEVENTF_RIGHTDOWN;

    mINPUT.mouseData = 0;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));
}

void VirtualMouse::rightButtonUp()
{
    MOUSEINPUT mINPUT;

    mINPUT.dwFlags = MOUSEEVENTF_RIGHTUP;

    mINPUT.mouseData = 0;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));
}

void VirtualMouse::leftClick()
{
    leftButtonDown();
    leftButtonUp();
}

void VirtualMouse::leftButtonDown()
{
    MOUSEINPUT mINPUT;

    mINPUT.dwFlags = MOUSEEVENTF_LEFTDOWN;

    mINPUT.mouseData = 0;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));
}

void VirtualMouse::leftButtonUp()
{
    MOUSEINPUT mINPUT;

    mINPUT.dwFlags = MOUSEEVENTF_LEFTUP;

    mINPUT.mouseData = 0;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));
}

void VirtualMouse::verticalScroll(qint32 dy)
{
    MOUSEINPUT mINPUT;

    mINPUT.dwFlags = MOUSEEVENTF_WHEEL;

    mINPUT.mouseData = WHEEL_DELTA;

    INPUT input;
    input.type = 0;
    input.mi = mINPUT;

    SendInput(1, &input, sizeof (INPUT));
}
