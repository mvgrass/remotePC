#include "commandmanager.h"

CommandManager::CommandManager(QObject *parent) : QObject(parent)
{
    mouse = new VirtualMouse(this);
}

void CommandManager::parseCommand(const QByteArray & command)
{

}
