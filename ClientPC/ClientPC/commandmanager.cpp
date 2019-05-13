#include "commandmanager.h"
#include <QDebug>

CommandManager::CommandManager(QObject *parent) : QObject(parent)
{
    mouse = new VirtualMouse(this);
}

void CommandManager::parseCommand(const QByteArray command)
{
    quint32 commandNumber = (quint8)command.at(0);
    commandNumber = (commandNumber<<8)|(quint8)command.at(1);
    switch(commandNumber){
    //MouseMove
    case 21:{
        qint32 dx = 0;
        qint32 dy = 0;

        dx = static_cast<quint8>(command.at(2));
        dx = (dx<<8)|static_cast<quint8>(command.at(3));
        dx = (dx<<8)|static_cast<quint8>(command.at(4));
        dx = (dx<<8)|static_cast<quint8>(command.at(5));

        dy = static_cast<quint8>(command.at(6));
        dy = (dy<<8)|static_cast<quint8>(command.at(7));
        dy = (dy<<8)|static_cast<quint8>(command.at(8));
        dy = (dy<<8)|static_cast<quint8>(command.at(9));

        mouse->move(-dx, -dy);

        break;
    }

    //LeftClick
    case 22:{
        mouse->leftClick();
        break;
    }

    //RightClick
    case 23:{
        mouse->rightClick();
        break;
    }

    //RightPressed
    case 24:{
        mouse->rightButtonDown();
        break;
    }

    //RightReleased
    case 25:{
        mouse->rightButtonUp();
        break;
    }

    //LeftPressed
    case 26:{
        mouse->leftButtonDown();
        break;
    }

    //LeftReleased
    case 27:{
        mouse->leftButtonUp();
        break;
    }

    //LeftDoubleClick
    case 28:{
        mouse->leftClick();
        mouse->leftClick();
        break;
    }

    //WheelScrolled
    case 29:{
        qint32 value = 0;

        value = static_cast<quint8>(command.at(2));
        value = (value<<8)|static_cast<quint8>(command.at(3));
        value = (value<<8)|static_cast<quint8>(command.at(4));
        value = (value<<8)|static_cast<quint8>(command.at(5));


        mouse->verticalScroll(value);
        break;
    }

    }
}
